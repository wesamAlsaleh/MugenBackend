package com.avocadogroup.mugen.users.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "API endpoints for managing users")
public class UserController {
    // API endpoint to get user details by user ID
    @GetMapping("/user-details")
    public String getUserDetails() {
        // Get the user details from the service layer


        // For demonstration purposes, returning a static message
        return "User details would be returned here.";
    }
}
