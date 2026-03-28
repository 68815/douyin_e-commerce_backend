package com.example.authentication_and_authorization.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
public class RateLimiterConfig {

    @Bean
    public KeyResolver userApiKeyResolver() {
        return exchange -> {
            String token = null;

            var cookies = exchange.getRequest().getCookies();
            if (null != cookies && cookies.containsKey("satoken")) {
                var satokenCookie = cookies.getFirst("satoken");
                if (null != satokenCookie) {
                    token = satokenCookie.getValue();
                }
            }

            if (null == token || token.isEmpty()) {
                token = "anonymous";
            }

            String path = exchange.getRequest().getURI().getPath();

            return Mono.just(token + "_" + path);
        };
    }

    @Bean
    public KeyResolver ipKeyResolver() {
        return exchange -> Mono.just(
            exchange.getRequest().getRemoteAddress() != null
                && exchange.getRequest().getRemoteAddress().getAddress() != null
                ? exchange.getRequest().getRemoteAddress().getAddress().getHostAddress()
                : "unknown"
        );
    }
}
