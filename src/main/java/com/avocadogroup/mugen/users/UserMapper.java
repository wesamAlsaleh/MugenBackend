package com.avocadogroup.mugen.users;

import com.avocadogroup.mugen.users.dtos.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring") // MapStruct will generate the implementation of this interface at compile time and register it as a Spring Bean
public interface UserMapper {
    UserDto toDto(User user);
}
