package org.example.cacheproxy.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;
import java.util.Optional;

public class RedisCacheService implements CacheService {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public RedisCacheService(StringRedisTemplate redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void put(String key, CachedResponse response, Duration ttl) {
        if (ttl == null || ttl.isZero() || ttl.isNegative()) return;
        try {
            String json = objectMapper.writeValueAsString(response);
            redisTemplate.opsForValue().set(key, json, ttl);
        } catch (Exception e) {
            e.printStackTrace(); // adaugă asta
        }
    }

    @Override
    public Optional<CachedResponse> get(String key) {
        String json = redisTemplate.opsForValue().get(key);
        System.out.println("Cache GET key=" + key + " value=" + json); // adaugă asta
        if (json == null) return Optional.empty();
        try {
            return Optional.of(objectMapper.readValue(json, CachedResponse.class));
        } catch (Exception e) {
            e.printStackTrace(); // adaugă asta
            return Optional.empty();
        }
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }
}
