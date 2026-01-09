package org.example.cacheproxy.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@ConfigurationProperties(prefix = "proxy")
public class ProxyProperties {
    private String upstreamBaseUrl;
    private int connectTimeoutMs;
    private int readTimeoutMs;
    private List<String> forwardHeaders;

    public String getUpstreamBaseUrl() {
        return upstreamBaseUrl;
    }

    public void setUpstreamBaseUrl(String upstreamBaseUrl) {
        this.upstreamBaseUrl = upstreamBaseUrl;
    }

    public int getConnectTimeoutMs() {
        return connectTimeoutMs;
    }

    public void setConnectTimeoutMs(int connectTimeoutMs) {
        this.connectTimeoutMs = connectTimeoutMs;
    }

    public int getReadTimeoutMs() {
        return readTimeoutMs;
    }

    public void setReadTimeoutMs(int readTimeoutMs) {
        this.readTimeoutMs = readTimeoutMs;
    }

    public List<String> getForwardHeaders() {
        return forwardHeaders;
    }

    public void setForwardHeaders(List<String> forwardHeaders) {
        this.forwardHeaders = forwardHeaders;
    }
}
