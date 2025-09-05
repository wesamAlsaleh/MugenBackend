package com.avocadogroup.mugen.authentication;

import com.avocadogroup.mugen.authentication.dtos.*;
import com.avocadogroup.mugen.authentication.services.AuthenticationService;
import com.avocadogroup.mugen.global.dtos.ErrorDto;
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
    public ResponseEntity<?> register(
            @Valid @RequestBody RegisterRequest request,
            UriComponentsBuilder uriBuilder
    ) {
        try {
            // Try to register the user and return the AuthDto
            var response = authenticationService.register(request);

            // Create the URI for the newly created user resource
            var uri = uriBuilder.path("/users/{id}").buildAndExpand(response.getUserId()).toUri();

            // Return a 201 response with the AuthDto in the body
            return ResponseEntity.created(uri).body(response.getJwtTokens());
        } catch (Exception e) {
            // In case of any exception, return a 400 Bad Request response
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // API Endpoint for user login (Mobile clients)
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

    // API Endpoint for token refresh (mobile clients) {web clients should use silent refresh with secure cookies 'HttpServletResponse'}
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(
            @Valid @RequestBody RefreshTokenRequest request
    ) {
        // Try to refresh the access token using the refresh token
        try {
        var tokens = authenticationService.refresh(request.getRefreshToken());

        // Return a 200 OK response with the new tokens in the body
        return ResponseEntity.ok(tokens);
        } catch (Exception e) {
            // In case of any exception (e.g., invalid refresh token), return a 401 Unauthorized response
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorDto("Session expired, please log in again"));
        }
    }

    // API Endpoint for changing the password
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @Valid @RequestBody ChangePasswordRequest request
            ) {
        try {
        // Try to change the user's password
        authenticationService.changePassword(request);

        // Return a 200 OK response with no body
        return ResponseEntity.ok().build();
        } catch (Exception e) {
            // In case of any exception return a 400 Bad Request response
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(e.getMessage()));
        }
    }

    // API Endpoint for forgot password (send OTP to email)
    @PostMapping("/request-forgot-password-otp")
    public ResponseEntity<?> requestForgotPasswordOtp(
            @Valid @RequestBody ForgotPasswordOtpRequest request
    ) {
        try {
            // Try to send the OTP to the user's email
            authenticationService.requestPasswordResetOtp(request.getEmail());

            // Return a 200 OK response with no body
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // In case of any exception return a 400 Bad Request response
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(e.getMessage()));
        }
    }

    // API endpoint for verifying password reset OTP
    @PostMapping("/verify-password-reset-otp")
    public ResponseEntity<?> verifyPasswordResetOtp(
            @Valid @RequestBody VerifyPasswordResetOtpRequest request
    ) {
        try {
            // Try to verify the OTP
            authenticationService.verifyPasswordResetOtp(request);

            // Return a 200 OK response with no body
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // In case of any exception return a 400 Bad Request response
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(e.getMessage()));
        }
    }

    // API endpoint for resetting password using OTP
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request
    ) {
        try {
            // Try to change the user's password after OTP verification
            authenticationService.resetPassword(request);

            // Return a 200 OK response with no body
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // In case of any exception return a 400 Bad Request response
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(e.getMessage()));
        }
    }

    // TODO: API Endpoint for logout (revoke refresh token)
}
