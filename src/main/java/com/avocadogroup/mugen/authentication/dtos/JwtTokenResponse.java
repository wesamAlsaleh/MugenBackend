package com.avocadogroup.mugen.authentication.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtTokenResponse {
//    private String accessToken;
//    private String refreshToken;
    private String token;
}
