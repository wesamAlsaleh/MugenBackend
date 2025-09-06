package com.avocadogroup.mugen.authentication.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class VerifyPasswordResetOtpRequest {
    @NotBlank(message = "Reset token must not be blank")
    @NotEmpty(message = "Reset token must not be empty")
    private String resetToken;

    @NotBlank(message = "OTP must not be blank")
    @NotEmpty(message = "OTP must not be empty")
    @Size(min = 6, max = 6, message = "OTP must be exactly 6 characters long")
    private String otp;
}
