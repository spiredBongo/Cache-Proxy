package org.example.cacheproxy.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

public interface ProxyService {
    ResponseEntity<String> forwardGet(HttpServletRequest request);
    String buildUpstreamUrl (HttpServletRequest request);
    HttpHeaders buildForwardHeaders(HttpServletRequest request);

}
