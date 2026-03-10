package org.example.cacheproxy.cache;

import org.springframework.http.HttpHeaders;

import java.time.Duration;
import java.util.Optional;

public interface CacheService {
    Optional<CachedResponse> get(String key);
    void put(String key, CachedResponse response, Duration ttl);
    void delete(String key);
}
