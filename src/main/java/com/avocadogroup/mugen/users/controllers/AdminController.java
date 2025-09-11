package com.avocadogroup.mugen.users.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// This controller will handle admin-specific endpoints

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin Management", description = "API endpoints for managing admin functionalities")
public class AdminController {
    // API endpoint to test admin access
    @GetMapping("/hi")
    public String hiAdmin() {
        return "Hi Admin!";
    }
}
