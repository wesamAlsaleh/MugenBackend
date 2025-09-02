package com.avocadogroup.mugen.authentication.dtos;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class RegisterRequest {
    @NotEmpty(message = "Username cannot be empty")
    @NotBlank(message = "Username cannot be blank")
    private String username;

    @NotEmpty(message = "Email cannot be empty")
    @NotBlank(message = "Email cannot be blank")
    @Email
    private String email;

    @NotEmpty(message = "Password cannot be empty")
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, max = 255, message = "Password must be between 8 and 255 characters")
    private String password;
}
