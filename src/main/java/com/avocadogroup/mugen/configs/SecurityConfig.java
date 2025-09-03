package com.avocadogroup.mugen.configs;

import com.avocadogroup.mugen.authentication.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // Mark this class as a configuration class to define beans at runtime
@EnableWebSecurity // Enable Spring Security's web security support
@AllArgsConstructor // Lombok annotation to generate a constructor with parameters for all fields
public class SecurityConfig {
    private final UserDetailsServiceImpl userDetailsService;

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
                request
                        .requestMatchers("/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/**").permitAll()
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

    // Bean to register DaoAuthenticationProvider at runtime (to be used by Spring Security for authentication)
    @Bean
    public AuthenticationProvider authenticationProvider() {
        // Create a new instance of DaoAuthenticationProvider
        var provider = new DaoAuthenticationProvider();

        // Set the Dao AuthenticationProvider's tools: UserDetailsService and PasswordEncoder
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder()); // Set BCryptPasswordEncoder as the password encoder for DaoAuthenticationProvider

        // Return the configured DaoAuthenticationProvider
        return provider;
    }

    // Bean to provide an AuthenticationManager at runtime (to be used for authentication in the application)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        // Return the AuthenticationManager from the AuthenticationConfiguration
        return authConfig.getAuthenticationManager();
    }
}
