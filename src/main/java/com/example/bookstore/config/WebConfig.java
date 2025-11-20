package com.example.bookstore.config;

import com.example.bookstore.security.ApiKeyInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @SuppressWarnings("unused") // field intentionally unused while API-key interceptor is commented out for local testing
    private final ApiKeyInterceptor apiKeyInterceptor;

    public WebConfig(ApiKeyInterceptor apiKeyInterceptor) {
        this.apiKeyInterceptor = apiKeyInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // NOTE: API key enforcement is temporarily disabled for easier local testing.
        // To re-enable API key checks, uncomment the line below. Ensure the API_KEY
        // environment variable is set or `api.key` is present in application.properties.

        // registry.addInterceptor(apiKeyInterceptor)
        //         .addPathPatterns("/api/**");
    }

    // Allow CORS for frontend running on http://localhost:3000
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("Authorization")
                .allowCredentials(true);
    }
}
