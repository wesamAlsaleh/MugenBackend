package com.avocadogroup.mugen.authentication.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

// This response is used for mobile clients, and it needs to return both access and refresh tokens
// While web clients only need the access token as they can use secure cookies for refresh tokens
// So, TODO: create a separate response class for web clients

@Data
@AllArgsConstructor
public class JwtTokenResponse {
    private String accessToken;
    private String refreshToken;
}
