package org.example.cacheproxy.proxy;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UpstreamClient {
    private final RestTemplate restTemplate;

    public UpstreamClient(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<String> get(String url, HttpHeaders headers) {
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
        );
    }
}
