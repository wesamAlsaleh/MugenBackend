package com.avocadogroup.mugen.users;

import com.avocadogroup.mugen.authentication.services.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private final AuthenticationService authenticationService;

    // Function to get the current user details
    public User getCurrentUser() {
        // Get the authenticated user from the security context
        var user = authenticationService.getCurrentUser();

        return user;
    }
}
