package com.avocadogroup.mugen.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // Mark this class as a configuration class to define beans at runtime
@EnableWebSecurity // Enable Spring Security's web security support
public class SecurityConfig {
    // Function to configure the security configuration
    @Bean // Define this method as a bean to be managed by Spring
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Set session management to stateless (token-based authentication not session-based authentication)
        http.sessionManagement(sessionManagementConfigurer ->
                        sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Disable CSRF (since we're using stateless sessions / APIs)
        http.csrf(AbstractHttpConfigurer::disable);

        // Define endpoint access rules (what endpoints that requires authentication and what not "Public/Permit")
        http.authorizeHttpRequests(request ->
                // Public endpoints (no authentication required)
                request.requestMatchers("/**").permitAll()
                // TODO: All other endpoints (authentication required)
                // TODO: Admins Endpoints
        );

        // Build and return the configured SecurityFilterChain (Configuration object to be used by Spring Security at runtime)
        return http.build();
    }

    // Bean to provide a PasswordEncoder at runtime to be used for hashing passwords in the application
    @Bean
    public PasswordEncoder passwordEncoder() {
        // Return a new instance of BCryptPasswordEncoder (which is a strong hashing algorithm)
        return new BCryptPasswordEncoder();
    }
}
