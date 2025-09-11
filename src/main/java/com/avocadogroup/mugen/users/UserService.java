package com.avocadogroup.mugen.users;

import com.avocadogroup.mugen.authentication.services.AuthenticationService;
import com.avocadogroup.mugen.global.exceptions.BadRequestException;
import com.avocadogroup.mugen.users.dtos.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private final AuthenticationService authenticationService;
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    // Function to get the currently authenticated user's details from the security context holder
    public UserDto me(){
        // Get the current user entity
        var user = authenticationService.getCurrentUser();

        // Return the user as a UserDto
        return userMapper.toDto(user);
    }

    // Function to update the currently authenticated user's details
    public void updateUser(String username, String email){
        // Get the current user entity
        var user = authenticationService.getCurrentUser();

        // Update the user's details if provided
        if (username != null && !username.isBlank()) {
            // Update username
            user.setUsername(username);
        }

        if (email != null && !email.isBlank()) {
            // Update email only if it's different from the current one
            if(!email.equals(user.getEmail())){
                // Check if the new email is already taken by another user
                if(userRepository.existsByEmail(email)){
                    throw new BadRequestException("Email already in use, please choose another one.");
                }

                // Update email if it's not taken and is different from the current one
                user.setEmail(email);
            }
        }

        // Save the updated user entity
        userRepository.save(user);
    }

}
