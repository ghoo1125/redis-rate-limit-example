package com.limit.redis.controller;

import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.config.RateLimitKeyGenerator;
import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.config.RateLimitUtils;
import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.config.properties.RateLimitProperties;
import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.support.DefaultRateLimitKeyGenerator;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.StringJoiner;

@Controller
public class PingController {
    @Bean
    public RateLimitKeyGenerator ratelimitKeyGenerator(RateLimitProperties properties, RateLimitUtils rateLimitUtils) {
        return new DefaultRateLimitKeyGenerator(properties, rateLimitUtils) {
            @Override
            public String key(HttpServletRequest request, Route route, RateLimitProperties.Policy policy) {
                final StringJoiner joiner = new StringJoiner(":");
                joiner.add(properties.getKeyPrefix());
                if (route != null) {
                    joiner.add(route.getPath());
                }
                joiner.add(request.getMethod());
                String key = joiner.toString();
                return key;
            }
        };
    }

    @GetMapping("/api/ping")
    public ResponseEntity<String> getSimple() {
        return ResponseEntity.ok("PONG");
    }

}
