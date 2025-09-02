package com.avocadogroup.mugen.authentication;

import com.avocadogroup.mugen.authentication.dtos.AuthDto;
import com.avocadogroup.mugen.authentication.dtos.RegisterRequest;
import com.avocadogroup.mugen.users.dtos.UserDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@AllArgsConstructor // Lombok annotation to generate a constructor with parameters for all fields
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    // API Endpoint for user registration
    @PostMapping("/register")
    public ResponseEntity<AuthDto> registerUser(
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
            return ResponseEntity.badRequest().build();
        }
    }

    // TODO: API Endpoint for user login
    // TODO: API Endpoint for token refresh
    // TODO: API Endpoint for password reset
    // TODO: API Endpoint for email verification
    // TODO: API Endpoint for logout
}
