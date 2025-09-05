package com.avocadogroup.mugen.authentication.services;

import com.avocadogroup.mugen.authentication.AuthenticationMapper;
import com.avocadogroup.mugen.authentication.dtos.*;
import com.avocadogroup.mugen.email.EmailService;
import com.avocadogroup.mugen.email.dtos.SimpleEmailRequest;
import com.avocadogroup.mugen.global.exceptions.BadRequestException;
import com.avocadogroup.mugen.global.exceptions.DuplicateResourceException;
import com.avocadogroup.mugen.global.exceptions.ResourceNotFoundException;
import com.avocadogroup.mugen.users.UserMapper;
import com.avocadogroup.mugen.users.UserRepository;
import com.avocadogroup.mugen.users.dtos.UserDto;
import com.avocadogroup.mugen.users.enums.UserPreferredLanguage;
import com.avocadogroup.mugen.users.enums.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationService {
    private final AuthenticationMapper authenticationMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final EmailService emailService;

    // Function to get the user id from the security context holder
    private Long getSecurityContextPrincipal(){
        // Get the Security Context Holder which holds the authentication information for the current request
        var authenticationObject = SecurityContextHolder.getContext().getAuthentication();

        // Extract and return the user ID from the authentication object principal (which we set in the AuthenticationFilter)
        return (Long) authenticationObject.getPrincipal(); // Cast the principal to Long (user ID) because spring doesn't know the type of the principal
    }

    // Function to handle user registration
    public RegisterResponse register(RegisterRequest request) {
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

        // Automatically log in the user after successful registration
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        ); // This will pass the credentials to DaoAuthenticationProvider which will use UserDetailsService to load user from the db and PasswordEncoder to verify password, if it matches, the user is authenticated and stored in the SecurityContextHolder

        // Generate an access token (JWT) for the newly registered user
        var accessToken = jwtService.generateAccessToken(user);

        // Generate a refresh token (JWT) for the newly registered user
        var refreshToken = jwtService.generateRefreshToken(user);

        // TODO: Save the refresh token in the database (if you want to implement refresh token revocation)

        // Wrap the tokens in a JwtTokenResponse object {accessToken:"abc", refreshToken:"xyz"}
        var jwtTokens = new JwtTokenResponse(accessToken, refreshToken);

        // Return the RegisterResponse containing the tokens and user ID
        return new RegisterResponse(jwtTokens, user.getId());
    }

    // Function to handle user login using DaoAuthenticationProvider (Spring Security)
    public JwtTokenResponse login(LoginRequest request) {
        // Call the authenticate method to perform authentication through the configured AuthenticationManager that uses DaoAuthenticationProvider
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())); // This will pass the credentials to DaoAuthenticationProvider which will use UserDetailsService to load user from the db and PasswordEncoder to verify password

        // Fetch the user from the db using the provided email
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(); // This should never throw since authentication would have failed earlier if user doesn't exist

        // Generate an access token (JWT) for the authenticated user
        var accessToken = jwtService.generateAccessToken(user);

        // Generate a refresh token (JWT) for the authenticated user
         var refreshToken = jwtService.generateRefreshToken(user);

        // TODO: Save the refresh token in the database (if you want to implement refresh token revocation)

        // Wrap and return the token in a JwtTokenResponse object {accessToken:"abc", refreshToken:"xyz"}
         return new JwtTokenResponse(accessToken, refreshToken);
    }

    // Function to get the currently authenticated user's details from the security context holder
    public UserDto me(){
        // Get the user ID from the security context holder
       var userId = getSecurityContextPrincipal();

        // Fetch the user from the database using the user ID
        var user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User not found")); // This should never throw since the user is authenticated

        // Return the user as a UserDto
        return userMapper.toDto(user);
    }

    // Function to refresh access token using a valid refresh token
    public JwtTokenResponse refresh(String refreshToken){
        // Check if the refresh token is expired
        if(jwtService.isTokenExpired(refreshToken)){
            // TODO: Revoke the refresh token in the database (if you want to implement refresh token revocation)

            throw new BadRequestException("Your session has expired, please log in again");
        }

        // Extract the user ID from the refresh token
        var userId = jwtService.getUserIdFromToken(refreshToken);

        // Fetch the user from the database using the user ID
        var user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User not found")); // This should never throw since the token is valid

        // Generate a new access token (JWT) for the user
        var newAccessToken = jwtService.generateAccessToken(user);

        // Refresh token rotation: Generate a new refresh token (JWT) for the user
        var newRefreshToken = jwtService.generateRefreshToken(user);

        // TODO: Save the refresh token in the database (if you want to implement refresh token revocation)

        // Wrap and return the new tokens in a JwtTokenResponse object {accessToken:"abc", refreshToken:"xyz"}
        return new JwtTokenResponse(newAccessToken, newRefreshToken);
    }

    // Function to handle password reset
     public void changePassword(ChangePasswordRequest request) {
        // Get the user id from the security context holder
        var userId = getSecurityContextPrincipal();

        // Get the user by their email
        var user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User not found")); // This should never throw since the user is authenticated

        // Check if the current password provided matches the user's actual current password
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException("Current password is incorrect");
        }

        // Update the user's password with the new password (hashed)
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // Save the updated user to the database
        userRepository.save(user);
     }
}
