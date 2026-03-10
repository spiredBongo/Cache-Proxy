package org.example.cacheproxy.service;


import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.example.cacheproxy.cache.CacheKeyBuilder;
import org.example.cacheproxy.cache.CachePolicy;
import org.example.cacheproxy.cache.CacheService;
import org.example.cacheproxy.cache.CachedResponse;
import org.example.cacheproxy.config.ProxyProperties;
import org.example.cacheproxy.proxy.UpstreamClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.Enumeration;
import java.util.Map;
import java.util.Optional;

@Service
public class ProxyServiceImpl implements ProxyService{
    private final ProxyProperties props;
    private final UpstreamClient upstreamClient;
    private final CacheService cacheService;
    private final CachePolicy cachePolicy;

    private static final Duration DEFAULT_TTL = Duration.ofMinutes(5);

    public ProxyServiceImpl(ProxyProperties props, UpstreamClient upstreamClient,
                            CacheService cacheService, CachePolicy cachePolicy) {
        this.props = props;
        this.upstreamClient = upstreamClient;
        this.cacheService = cacheService;
        this.cachePolicy = cachePolicy;
    }

    @Override
    public ResponseEntity<String> forwardAll(HttpServletRequest request, byte[] body) {
        HttpMethod method = HttpMethod.valueOf(request.getMethod());
        HttpHeaders headersToForward = buildForwardHeaders(request);
        String upstreamUrl = buildUpstreamUrl(request);

        boolean useCache = cachePolicy.shouldUseCache(method)
                && !cachePolicy.shouldBypass(headersToForward);

        if (useCache) {
            String key = CacheKeyBuilder.build(request);
            Optional<CachedResponse> cached = cacheService.get(key);
            if (cached.isPresent()) {
                CachedResponse cr = cached.get();
                HttpHeaders responseHeaders = new HttpHeaders();
                cr.headers().forEach(responseHeaders::add);
                responseHeaders.add("X-Cache", "HIT");
                return ResponseEntity.status(cr.statusCode())
                        .headers(responseHeaders)
                        .body(cr.body() != null ? new String(cr.body()) : null);
            }

            ResponseEntity<String> response = upstreamClient.forward(upstreamUrl, request, headersToForward, method, body);

            if (cachePolicy.isCacheable(response)) {
                Map<String, String> flatHeaders = response.getHeaders().toSingleValueMap();
                byte[] responseBody = response.getBody() != null ? response.getBody().getBytes() : new byte[0];
                CachedResponse toCache = new CachedResponse(
                        response.getStatusCode().value(),
                        flatHeaders,
                        responseBody
                );
                cacheService.put(key, toCache, DEFAULT_TTL);
            }

            return response;
        }

        return upstreamClient.forward(upstreamUrl, request, headersToForward, method, body);
    }

    @Override
    public String buildUpstreamUrl(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String prefix = contextPath + "/proxy";

        String pathAfterProxy = requestUri.startsWith(prefix)
                ? requestUri.substring(prefix.length())
                : requestUri;

        if (pathAfterProxy.isEmpty()) pathAfterProxy = "/";

        String query = request.getQueryString();
        String base = props.getUpstreamBaseUrl();

        boolean baseEndsWithSlash = base.endsWith("/");
        boolean pathStartsWithSlash = pathAfterProxy.startsWith("/");

        String normalized =
                (baseEndsWithSlash && pathStartsWithSlash) ? base.substring(0, base.length() - 1) + pathAfterProxy :
                        (!baseEndsWithSlash && !pathStartsWithSlash) ? base + "/" + pathAfterProxy :
                                base + pathAfterProxy;

        return (query == null || query.isBlank()) ? normalized : normalized + "?" + query;
    }

    @Override
    public HttpHeaders buildForwardHeaders(HttpServletRequest request) {
        HttpHeaders out = new HttpHeaders();
        for (String allowed : props.getForwardHeaders()) {
            Enumeration<String> values = request.getHeaders(allowed);
            while (values != null && values.hasMoreElements()) {
                out.add(allowed, values.nextElement());
            }
        }
        return out;
    }
}
