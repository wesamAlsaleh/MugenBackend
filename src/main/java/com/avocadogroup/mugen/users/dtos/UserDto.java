package com.avocadogroup.mugen.users.dtos;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String role;
    private String preferredLanguage;
}
