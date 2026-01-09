package org.example.cacheproxy.service;


import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.example.cacheproxy.config.ProxyProperties;
import org.example.cacheproxy.proxy.UpstreamClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.Enumeration;

@Service
public class ProxyServiceImpl implements ProxyService{
    private final ProxyProperties props;
    private final UpstreamClient upstreamClient;

    public ProxyServiceImpl(ProxyProperties props, UpstreamClient upstreamClient)
    {
        this.props = props;
        this.upstreamClient = upstreamClient;
    }

    public ResponseEntity<String> forwardGet(HttpServletRequest request){
        String upstreamUrl = buildUpstreamUrl(request);
        HttpHeaders headersToForward = buildForwardHeaders(request);

        return upstreamClient.get(upstreamUrl, headersToForward);
    }

    public String buildUpstreamUrl(HttpServletRequest request){
        String requestUri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String prefix = contextPath + "/proxy";

        String PathAfterProxy = requestUri.startsWith(prefix)
                ? requestUri.substring(prefix.length())
                : requestUri;

        if( PathAfterProxy.isEmpty()){
            PathAfterProxy = "/";
        }

        String query = request.getQueryString();

        String base = props.getUpstreamBaseUrl();
        boolean baseEndsWithSlash = base.endsWith("/");
        boolean pathStartsWithSlash = PathAfterProxy.startsWith("/");

        String normalized =
                (baseEndsWithSlash && pathStartsWithSlash) ? base.substring(0, base.length() - 1 ) + PathAfterProxy :
                        (!baseEndsWithSlash && !pathStartsWithSlash) ? base + "/" + PathAfterProxy :
                                base + PathAfterProxy;

        return (query == null || query.isBlank()) ? normalized : (normalized + "?" + query);
    }

    public HttpHeaders buildForwardHeaders(HttpServletRequest request){
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
