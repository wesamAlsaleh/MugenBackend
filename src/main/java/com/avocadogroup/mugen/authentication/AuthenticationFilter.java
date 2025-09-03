package com.avocadogroup.mugen.authentication;


import com.avocadogroup.mugen.authentication.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

// A filter that is executed once per request. It will run before any controller is called.
// This filter will check if the request contain a valid JWT token in the Authorization header.
// If the token is valid, it will check if the user has access to the requested resource.

// SecurityContextHolder store the authentication information of the current user in a thread-local storage.
// This means that the authentication information is stored in a variable that is specific to the current thread
// and is not shared with other threads. This is important because each request is handled by a different thread.
// By using thread-local storage, we can ensure that the authentication information is only accessible
// to the thread that is handling the current request. This prevents any potential security issues that could arise
// from sharing authentication information between different requests or users.

@Component // Register this filter as a Spring Component so that Spring can manage its lifecycle and dependencies
@AllArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    // Filter the request to check if the user can access protected resources
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Extract the JWT token from the Authorization header "Bearer A2C4"
        String authorizationHeader = request.getHeader("Authorization");

        // If the Authorization header is missing or does not start with "Bearer " then skip the filter
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            // If so, skip JWT validation and continue with the next filter in the chain
            filterChain.doFilter(request, response);

            // Exit the current filter and the spring security will handle the request as unauthenticated and return 401 Unauthorized
            return;
        }

        // Extract the token from the header
        var token = authorizationHeader.replace("Bearer ", ""); // Remove "Bearer "

        // If token expired then skip the filter
        if (jwtService.isTokenExpired(token)) {
            // If so, skip JWT validation and continue with the next filter in the chain
            filterChain.doFilter(request, response);

            // Exit the current filter and the spring security will handle the request as unauthenticated and return 401 Unauthorized
            return;
        }

        // Get the user ID from the token subject
        var userId = jwtService.getUserIdFromToken(token);

        // Get the user role from the token claims
        var userRole = jwtService.getUserRoleFromToken(token);

        // Build an authentication token object with the user ID and role (used by Spring Security to represent the authenticated user)
        var authenticationTokenObject = new UsernamePasswordAuthenticationToken(
                userId, // User id
                null, // No credentials because we are using JWT token for authentication (not username and password)
                List.of(new SimpleGrantedAuthority("ROLE_" + userRole)) // Set the user role
        );

        // Set the request details in the authentication token object (IP address, session ID, etc.) `boilerplate code!`
        authenticationTokenObject.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request) // Set the request details in the authentication token to be used by Spring Security
        );

        // Set the authentication object in the Security Context Holder (store the authentication information for the current request) (This will mark the user as authenticated in the current request context) `boilerplate code!`
        SecurityContextHolder.getContext().setAuthentication(authenticationTokenObject);

        // Continue with the next filter in the chain
        filterChain.doFilter(request, response); // Pass the control to the next filter method
    }
}
