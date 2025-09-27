package com.avocadogroup.mugen.authentication.services;

import com.avocadogroup.mugen.authentication.AuthenticationMapper;
import com.avocadogroup.mugen.authentication.dtos.*;
import com.avocadogroup.mugen.email.EmailService;
import com.avocadogroup.mugen.email.dtos.SimpleEmailRequest;
import com.avocadogroup.mugen.global.exceptions.BadRequestException;
import com.avocadogroup.mugen.global.exceptions.DuplicateResourceException;
import com.avocadogroup.mugen.global.exceptions.ResourceNotFoundException;
import com.avocadogroup.mugen.otp.PasswordResetOtp;
import com.avocadogroup.mugen.otp.PasswordResetOtpRepository;
import com.avocadogroup.mugen.users.User;
import com.avocadogroup.mugen.users.UserMapper;
import com.avocadogroup.mugen.users.UserRepository;
import com.avocadogroup.mugen.users.dtos.UserDto;
import com.avocadogroup.mugen.users.enums.UserPreferredLanguage;
import com.avocadogroup.mugen.users.enums.UserRole;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@AllArgsConstructor
public class AuthenticationService {
    private final AuthenticationMapper authenticationMapper;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;
    private final PasswordResetOtpRepository passwordResetOtpRepository;

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

    // Function to get the current user from the security context
    public User getCurrentUser(){
        // Get the user ID from the security context holder
        var userId = getSecurityContextPrincipal();

        // If no user ID found return null (this should never happen since the user is authenticated)
        if(userId == null) return null;

        // Fetch the user from the database using the user ID
        return userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User not found")); // This should never throw since the user is authenticated
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

     // Function to send an email with a password reset OTP
     public ForgotPasswordOtpResponse requestPasswordResetOtp(String email) {
        // Check if a user with the given email exists
        var user = userRepository.findByEmail(email)
                .orElseThrow(()-> new ResourceNotFoundException("User not found"));

        // Get the latest OTP for the user
        var latestOtp = passwordResetOtpRepository.findLatestByUserId(user.getId())
                .orElse(null);

        // If there is an existing OTP
        if (latestOtp != null) {
          // Check if the OTP is not expired and not used
          if (!latestOtp.isExpired() || latestOtp.isUsed()) {
              throw new BadRequestException("An OTP has already been sent to your email. Please check your inbox.");
          }
        }

        // Generate a new OTP code (6-digit random number as a string)
        var otpCode = String.format("%06d", (int)(Math.random() * 1000000)); // Generate a random 6-digit OTP code as a string

        // Create a new PasswordResetOtp entity and set its properties
        var passwordResetOtp = new PasswordResetOtp();

        passwordResetOtp.setUser(user);
        passwordResetOtp.setOtpCode(otpCode);

         // Set expiry as Instant (Current time + 15 minutes)
         Instant expiryInstant = Instant.now().plusSeconds(15 * 60); // OTP valid for 15 minutes (15 min each 60 sec)
         passwordResetOtp.setExpiry(expiryInstant);

        // Send the OTP to the user's email
         emailService.sendEmail(
                 new SimpleEmailRequest(
                         user.getEmail(),
                         "Password Reset Code - Mugen",
                         "Your password reset code is: " + otpCode + "\nThis code will expired in 15 minutes."
                 )
         );

        // Save the new OTP to the database
        passwordResetOtpRepository.save(passwordResetOtp);

        // Generate a reset token (JWT) for the user
         var resetToken = jwtService.generateResetPasswordToken(user);

        // Return the reset token in the response
         return new ForgotPasswordOtpResponse(resetToken);
     }

     // Function to verify the password reset OTP
    @Transactional // Mark the method as transactional to ensure atomicity of operations (either all checks + cleanup happen, or none)
    public void verifyPasswordResetOtp(VerifyPasswordResetOtpRequest request) {
        // If the reset token is expired, throw an error
        if (jwtService.isTokenExpired(request.getResetToken())) {
            throw new BadRequestException("Your reset token has expired, please request a new one");
        }

        // Get the user ID from the reset token (JWT)
        var userId = jwtService.getUserIdFromToken(request.getResetToken());

        // Get the user OTP from the database using the user ID and fetch the user eagerly
        var otpObj = passwordResetOtpRepository.findLatestOtpByUserIdWithUser(userId)
                .orElseThrow(()-> new BadRequestException("Invalid or expired OTP")); // If no OTP found, throw invalid OTP error

        // Check if the OTP belongs to the user from the token
        if (!otpObj.getUser().getId().equals(userId)) {
            throw new BadRequestException("Invalid or expired OTP");
        }

        // Check if the provided OTP matches the latest OTP
        if (!otpObj.getOtpCode().equals(request.getOtp())) {
            throw new BadRequestException("Invalid or expired OTP");
        }

        // Check if the OTP is expired or not used (if it's not used and not expired, it's still valid so don't generate an OTP again)
        if (otpObj.isExpired() || otpObj.isUsed()) {
            throw new BadRequestException("Your reset code has expired, please request a new one");
        }

        // Verify successful, mark the OTP as used
        otpObj.markAsUsed();

        // Delete all OTPs for the user (for security)
        passwordResetOtpRepository.deleteAllByUser(otpObj.getUser());
    }

    // Function to reset the password after OTP verification
    public void resetPassword(ResetPasswordRequest request) {
        // If the reset token is expired, throw an error
        if (jwtService.isTokenExpired(request.getResetToken())) {
            throw new BadRequestException("Your reset token has expired, please request a new one");
        }

        // Get the user ID from the reset token (JWT)
        var userId = jwtService.getUserIdFromToken(request.getResetToken());

        // Get the user from the database using the user ID
         var user = userRepository.findById(userId)
                 .orElseThrow(()-> new ResourceNotFoundException("User not found"));

        // Change the user's password to the new password (hashed)
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // Save the updated user to the database
        userRepository.save(user);

        // TODO: Destroy all existing refresh tokens for the user (if you want to implement refresh token revocation)
    }
}
