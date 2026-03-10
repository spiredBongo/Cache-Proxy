package org.example.cacheproxy.cache;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.net.http.HttpResponse;

public class CachePolicy {

    public boolean shouldUseCache(HttpMethod method){
        return HttpMethod.GET.equals(method);
    }

    public  boolean shouldBypass(HttpHeaders headers){
        return headers.containsHeader(HttpHeaders.AUTHORIZATION)|| headers.containsHeader(HttpHeaders.COOKIE);
    }

    public boolean isCacheable(ResponseEntity<String> response){
        return response.getStatusCode().value() == 200;
    }

}
