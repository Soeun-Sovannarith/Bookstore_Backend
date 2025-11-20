package com.example.bookstore.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader("Authorization");

        String username = null;
        String jwtToken = null;

        // Log incoming Authorization header and request info for debugging
        if (logger.isDebugEnabled()) {
            logger.debug("Incoming request: {} {}", request.getMethod(), request.getRequestURI());
            logger.debug("Incoming Authorization header: {}", requestTokenHeader);
        }

        // JWT Token is in the form "Bearer token". Remove Bearer word and get only the Token
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(jwtToken);
            } catch (Exception e) {
                // Log exception detail to help debugging invalid tokens
                logger.warn("JWT Token has expired or is invalid: {}", e.getMessage());
                if (logger.isDebugEnabled()) {
                    logger.debug("Token parsing exception", e);
                }
            }
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("Authorization header missing or does not start with 'Bearer '");
            }
        }

        // Once we get the token validate it.
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                if (jwtUtil.validateToken(jwtToken, username)) {
                    String role = jwtUtil.extractRole(jwtToken);

                    // Avoid double-prefixing the role: if the role already starts with ROLE_ use it as-is
                    String authorityStr = (role != null && role.startsWith("ROLE_")) ? role : "ROLE_" + role;

                    SimpleGrantedAuthority authority = new SimpleGrantedAuthority(authorityStr);

                    UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(username, null, Collections.singletonList(authority));
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    logger.info("Authenticated JWT user='{}' with authority='{}' for {} {}", username, authorityStr, request.getMethod(), request.getRequestURI());
                } else {
                    logger.warn("JWT validation failed for user={}", username);
                }
            } catch (Exception e) {
                logger.warn("Error validating JWT token: {}", e.getMessage());
                if (logger.isDebugEnabled()) {
                    logger.debug("Token validation exception", e);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
