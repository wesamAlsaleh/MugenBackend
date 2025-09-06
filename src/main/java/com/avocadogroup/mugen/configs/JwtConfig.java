package com.avocadogroup.mugen.configs;

import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

@Configuration
@ConfigurationProperties(prefix = "spring.jwt") // Prefix to bind properties from application.yaml
@Data
public class JwtConfig {
    // ******** This properties will be loaded from application.yaml ********
    private String secretKey; // Secret key for signing the JWT
    private long accessTokenExpirationTime; // Access token expiration time in seconds (900 seconds = 15 minutes)
    private long refreshTokenExpirationTime; // Refresh token expiration time in seconds (604800 seconds = 7 days)
    private long resetPasswordTokenExpirationTime; // Reset password token expiration time in seconds (3600 seconds = 1 hour)

    // Function to get the SecretKey object from the secret key string
    public SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

}
