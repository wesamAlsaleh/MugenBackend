package com.avocadogroup.mugen.authentication.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterResponse {
    private JwtTokenResponse jwtTokens;
    private Long userId;
}
