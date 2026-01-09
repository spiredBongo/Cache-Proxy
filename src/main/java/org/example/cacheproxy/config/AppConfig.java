package org.example.cacheproxy.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.restclient.RestTemplateBuilder;

import java.lang.reflect.Proxy;
import java.time.Duration;

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
}
