package com.avocadogroup.mugen.configs;

import com.avocadogroup.mugen.authentication.AuthenticationFilter;
import com.avocadogroup.mugen.authentication.UserDetailsServiceImpl;
import com.avocadogroup.mugen.users.enums.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
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
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // Mark this class as a configuration class to define beans at runtime
@EnableWebSecurity // Enable Spring Security's web security support
@AllArgsConstructor // Lombok annotation to generate a constructor with parameters for all fields
public class SecurityConfig {
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationFilter authenticationFilter;

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
                request
                // Public endpoints (no authentication required)
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/refresh").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/request-forgot-password-otp").permitAll()
                        .requestMatchers(HttpMethod.POST,"auth/verify-password-reset-otp").permitAll()
                        .requestMatchers(HttpMethod.POST,"auth/reset-password").permitAll()
                        .requestMatchers(HttpMethod.GET, "anime/this-season").permitAll()
                        .requestMatchers(HttpMethod.GET, "anime/top-this-season-animes").permitAll()
                // Role Based Endpoints (Requires specific role)
                        .requestMatchers("/admin/**").hasRole(UserRole.ADMIN.name()) // Only users with ADMIN role can access /admin/**
                // All other endpoints (authentication token required)
                        .anyRequest().authenticated()
        )
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class) // Add the custom AuthenticationFilter before the UsernamePasswordAuthenticationFilter which is a built-in filter in Spring Security that processes authentication requests
                .exceptionHandling(
                    exceptionHandler ->{
                        // Tell Spring Security to return 401 `Unauthorized` for unauthenticated requests instead of returning 403 `Forbidden`
                        exceptionHandler.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));

                        // Tell Spring Security to return 403 `Forbidden` for unauthorized requests (authenticated but not authorized, need specific role/permission)
                        exceptionHandler.accessDeniedHandler(
                                (request,
                                 response,
                                 accessDeniedException) ->
                                response.setStatus(HttpStatus.FORBIDDEN.value()));
                    }); // Handle exceptions (e.g., return 401 for unauthorized requests)


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
