package com.example.bookstore.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * API key interceptor is currently disabled. All requests pass through.
 * To re-enable, restore the header check logic and inject the property:
 *   @Value("${api.key}") private String API_KEY;
 * And perform validation when a handler method is annotated with {@link ApiKeyRequired}.
 */
@Component
public class ApiKeyInterceptor implements HandlerInterceptor {

    // Previously: @Value("${api.key}") private String API_KEY;
    private static final String API_KEY_HEADER = "X-API-Key"; // retained for documentation/reference

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // Short-circuit: feature turned off, do nothing.
        // If re-enabled, check if handler is a HandlerMethod and method has @ApiKeyRequired.
        // Then compare provided header with configured API_KEY.
        return true;
    }
}
