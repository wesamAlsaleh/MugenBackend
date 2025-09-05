package com.avocadogroup.mugen.authentication.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ForgotPasswordOtpRequest {
    @NotBlank(message = "Email is required")
    @Email
    private String email;
}
