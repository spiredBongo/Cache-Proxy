package org.example.cacheproxy.config;

import org.example.cacheproxy.cache.CachePolicy;
import org.example.cacheproxy.cache.RedisCacheService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.restclient.RestTemplateBuilder;
import java.time.Duration;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableConfigurationProperties(ProxyProperties.class)
public class AppConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder, ProxyProperties properties) {
        return builder
                .connectTimeout(Duration.ofMillis((properties.getConnectTimeoutMs())))
                .readTimeout(Duration.ofMillis(properties.getReadTimeoutMs()))
                .build();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public RedisCacheService cacheService(StringRedisTemplate redisTemplate, ObjectMapper objectMapper) {
        return new RedisCacheService(redisTemplate, objectMapper);
    }

    @Bean
    public CachePolicy cachePolicy() {
        return new CachePolicy();
    }
}

