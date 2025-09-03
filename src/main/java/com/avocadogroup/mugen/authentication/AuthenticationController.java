package com.avocadogroup.mugen.authentication;

import com.avocadogroup.mugen.authentication.dtos.LoginRequest;
import com.avocadogroup.mugen.authentication.dtos.RegisterRequest;
import com.avocadogroup.mugen.authentication.services.AuthenticationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@AllArgsConstructor // Lombok annotation to generate a constructor with parameters for all fields
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    // API Endpoint for user registration
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(
            @Valid @RequestBody RegisterRequest request,
            UriComponentsBuilder uriBuilder
    ) {
        try {
            // Try to register the user and return the AuthDto
            var authDto = authenticationService.register(request);

            // Create the URI for the newly created user resource
            var uri = uriBuilder.path("/users/{id}").buildAndExpand(authDto.getId()).toUri();

            // Return a 201 response with the AuthDto in the body
            return ResponseEntity.created(uri).body(authDto);
        } catch (Exception e) {
            // In case of any exception, return a 400 Bad Request response
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // API Endpoint for user login
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequest request
    ) {
        try{
        // Try to log in the user and return the JwtTokenResponse (if credentials are invalid, an exception will be thrown)
        var tokens = authenticationService.login(request);

        // Return a 200 OK response with the JwtTokenResponse in the body
        return ResponseEntity.ok(tokens);
        } catch (Exception e) {
            // In case of any exception (e.g., invalid credentials), return a 401 Unauthorized response
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    // API Endpoint for user profile (get current user info)
    @GetMapping("/me")
    public ResponseEntity<?> me() {
        try{
        // Try to get the current authenticated user's details
        var userDto = authenticationService.me();

        // Return a 200 OK response with the UserDto in the body
        return ResponseEntity.ok(userDto);
        } catch (Exception e) {
            // In case of any exception (e.g., user not authenticated), return a 401 Unauthorized response
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized, " + e.getMessage());
        }
    }

    // TODO: API Endpoint for token refresh
    // TODO: API Endpoint for password reset
    // TODO: API Endpoint for email verification
    // TODO: API Endpoint for logout
}
