package com.avocadogroup.mugen.users.dtos;

import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String role;
    private String preferredLanguage;
    private LocalDateTime createdAt;
}
