package com.avocadogroup.mugen.users.controllers;

import com.avocadogroup.mugen.users.UserService;
import com.avocadogroup.mugen.users.dtos.UpdateUserRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "API endpoints for managing users")
public class UserController {
    private final UserService userService;

    // API Endpoint for user profile (get current user info)
    @GetMapping("/me")
    public ResponseEntity<?> me() {
        try{
            // Try to get the current authenticated user's details
            var userDto = userService.me();

            // Return a 200 OK response with the UserDto in the body
            return ResponseEntity.ok(userDto);
        } catch (Exception e) {
            // In case of any exception (e.g., user not authenticated), return a 401 Unauthorized response
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized, " + e.getMessage());
        }
    }

    // API Endpoint for updating user profile (update current user info such as username, email)
    @PatchMapping("/me")
    public ResponseEntity<?> updateUser(
            @Valid @RequestBody UpdateUserRequest request
    ) {
        try{
            // Try to update the current authenticated user's details
            userService.updateUser(request.getUsername(), request.getEmail());

            // Return a 200 OK response with no content
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
