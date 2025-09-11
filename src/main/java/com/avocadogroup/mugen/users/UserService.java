package com.avocadogroup.mugen.users;

import com.avocadogroup.mugen.authentication.services.AuthenticationService;
import com.avocadogroup.mugen.users.dtos.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private final AuthenticationService authenticationService;
    private final UserMapper userMapper;

    // Function to get the currently authenticated user's details from the security context holder
    public UserDto me(){
        // Get the current user entity
        var user = authenticationService.getCurrentUser();

        // Return the user as a UserDto
        return userMapper.toDto(user);
    }

    //

}
