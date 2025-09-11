package com.avocadogroup.mugen.users.dtos;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UpdateUserRequest {
    private String username;

    @Email
    private String email;
}
