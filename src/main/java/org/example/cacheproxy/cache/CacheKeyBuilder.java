package org.example.cacheproxy.cache;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;

public class CacheKeyBuilder {

    public static String build(HttpServletRequest request) {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String query = request.getQueryString();

        String base = method + ":" + uri;
        return (query == null || query.isBlank()) ? base : base + "?" + query;
    }
}
