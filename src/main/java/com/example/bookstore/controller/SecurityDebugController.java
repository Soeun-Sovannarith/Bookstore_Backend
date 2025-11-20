// explanation: add a debug controller that returns current Authentication details for troubleshooting 403
package com.example.bookstore.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/secure")
public class SecurityDebugController {

    @GetMapping("/auth")
    public ResponseEntity<?> authInfo(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.ok(Map.of("authenticated", false));
        }
        List<String> authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(Map.of(
                "authenticated", authentication.isAuthenticated(),
                "name", authentication.getName(),
                "principal_class", authentication.getPrincipal().getClass().getName(),
                "authorities", authorities
        ));
    }
}

