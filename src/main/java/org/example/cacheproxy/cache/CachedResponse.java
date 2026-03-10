package org.example.cacheproxy.cache;

import java.util.Map;

public record CachedResponse(
        int statusCode,
        Map<String, String> headers,
        byte[] body
) {}
