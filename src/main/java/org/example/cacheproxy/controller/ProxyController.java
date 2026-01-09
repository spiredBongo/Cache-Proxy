package org.example.cacheproxy.controller;


import jakarta.servlet.http.HttpServletRequest;
import org.example.cacheproxy.service.ProxyServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/proxy")
public class ProxyController {
    private final ProxyServiceImpl proxyService;

    public ProxyController(ProxyServiceImpl proxyService)
    {
        this.proxyService = proxyService;
    }

    @GetMapping("/**")
    public ResponseEntity<String> proxyGet(HttpServletRequest request){
        return proxyService.forwardGet(request);
    }
}
