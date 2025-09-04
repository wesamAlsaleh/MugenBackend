package com.avocadogroup.mugen.authentication;

import com.avocadogroup.mugen.users.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

// Note that UserDetailsService is a core interface in Spring Security, and it expects exception to UsernameNotFoundException, not custom exceptions

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    // This method should load user details by username (or email in this case)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Get the user by email or throw UsernameNotFoundException if not found
        var user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found"));

        // If the user found return the UserDetails object using org.springframework.security.core.userdetails.User class
        return new User(
            user.getEmail(), // Email
            user.getPassword(), // Password
            Collections.emptyList() // Authorities (roles/permissions - empty for now)
        );
    }
}
