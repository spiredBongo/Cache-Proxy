package org.example.cacheproxy.proxy;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.Response;
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

    public ResponseEntity<String> forward(String url, HttpServletRequest request, HttpHeaders headers,HttpMethod method, byte[] body){

        HttpEntity<byte[]> entity = new HttpEntity<>(body, headers);

        return restTemplate.exchange(
                url,
                method,
                entity,
                String.class
        );

    }
}
