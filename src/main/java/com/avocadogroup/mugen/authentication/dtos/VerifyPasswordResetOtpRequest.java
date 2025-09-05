package com.avocadogroup.mugen.authentication.dtos;

import lombok.Data;

@Data
public class VerifyPasswordResetOtpRequest {
    private String email;
    private String otp;
}
