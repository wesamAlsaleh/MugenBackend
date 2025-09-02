package com.avocadogroup.mugen.authentication;

import com.avocadogroup.mugen.authentication.dtos.AuthDto;
import com.avocadogroup.mugen.authentication.dtos.RegisterRequest;
import com.avocadogroup.mugen.users.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring") // MapStruct annotation to define this interface as a mapper and generate implementation at compile time
public interface AuthenticationMapper {
    // Create User entity from RegisterRequest DTO
    User toEntity(RegisterRequest request);

    // Register Response DTO from User entity
    AuthDto toDto(User user);
}
