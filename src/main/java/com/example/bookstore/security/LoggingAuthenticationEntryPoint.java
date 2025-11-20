// Add a custom AuthenticationEntryPoint that logs unauthenticated access attempts for debugging
package com.example.bookstore.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoggingAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.AuthenticationException authException) throws IOException, ServletException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            logger.warn("Unauthenticated access attempt by user='{}' with authorities={} to {} {}. Reason: {}", auth.getName(), auth.getAuthorities(), request.getMethod(), request.getRequestURI(), authException.getMessage());
        } else {
            logger.warn("Unauthenticated access attempt (anonymous) to {} {}. Reason: {}", request.getMethod(), request.getRequestURI(), authException.getMessage());
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"Authentication is required to access this resource\"}");
    }
}

