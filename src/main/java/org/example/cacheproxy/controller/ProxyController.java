package org.example.cacheproxy.controller;


import jakarta.servlet.http.HttpServletRequest;
import org.example.cacheproxy.service.ProxyServiceImpl;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/proxy")
public class ProxyController {
    private final ProxyServiceImpl proxyService;

    public ProxyController(ProxyServiceImpl proxyService)
    {
        this.proxyService = proxyService;
    }

    @GetMapping("/**")
    public ResponseEntity<String> proxyGet(HttpServletRequest request, @RequestBody(required = false) byte[] body){
        return proxyService.forwardAll(request, body);
    }

    @PostMapping("/**")
    public ResponseEntity<String> proxyPost(HttpServletRequest request, @RequestBody byte[] body){
        return proxyService.forwardAll(request, body);
    }

    @DeleteMapping("/**")
    public ResponseEntity<String> proxyDelete(HttpServletRequest request, @RequestBody(required = false) byte[] body){
        return proxyService.forwardAll(request, body);
    }
    @PutMapping("/**")
    public ResponseEntity<String> proxyUpdate(HttpServletRequest request, @RequestBody byte[] body){
        return proxyService.forwardAll(request, body);
    }
}
