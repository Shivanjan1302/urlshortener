package com.example.urlshortener.rateLimit;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter implements Filter {

    private final ConcurrentHashMap<String, RequestInfo> map = new ConcurrentHashMap<>();

    private static class RequestInfo {
        int count = 0;
        long timestamp = Instant.now().getEpochSecond();
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        String ip = request.getRemoteAddr();

        long now = Instant.now().getEpochSecond();
        RequestInfo info = map.getOrDefault(ip, new RequestInfo());

        if (now - info.timestamp >= 60) {
            info.count = 0;
            info.timestamp = now;
        }

        info.count++;
        map.put(ip, info);

        if (info.count > 30) {
            HttpServletResponse response = (HttpServletResponse) res;
            response.setStatus(429);
            response.getWriter().write("Too Many Requests");
            return;
        }

        chain.doFilter(req, res);
    }
}
