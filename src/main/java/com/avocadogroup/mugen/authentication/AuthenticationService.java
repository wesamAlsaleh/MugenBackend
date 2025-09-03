package com.avocadogroup.mugen.authentication;

import com.avocadogroup.mugen.authentication.dtos.AuthDto;
import com.avocadogroup.mugen.authentication.dtos.LoginRequest;
import com.avocadogroup.mugen.authentication.dtos.RegisterRequest;
import com.avocadogroup.mugen.global.exceptions.DuplicateResourceException;
import com.avocadogroup.mugen.users.UserRepository;
import com.avocadogroup.mugen.users.enums.UserPreferredLanguage;
import com.avocadogroup.mugen.users.enums.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationService {
    private final AuthenticationMapper authenticationMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    // Function to handle user registration
    public AuthDto register(RegisterRequest request) {
        // Check if a user with the given email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }

        // Create a new user entity from the registration request
        var user = authenticationMapper.toEntity(request);

        // Hash the user's password before saving it to the database
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Set the default user role and preferred language
        user.setRole(UserRole.USER);
        user.setPreferredLanguage(UserPreferredLanguage.EN);

        // Save the new user to the database
        userRepository.save(user);

        // Return the saved user as a AuthDto
        return authenticationMapper.toDto(user);
    }

    // Function to handle user login using DaoAuthenticationProvider (Spring Security)
    public void login(LoginRequest request) {
        // Call the authenticate method to perform authentication through the configured AuthenticationManager that uses DaoAuthenticationProvider
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())); // This will pass the credentials to DaoAuthenticationProvider which will use UserDetailsService to load user and PasswordEncoder to verify password
    }
}
