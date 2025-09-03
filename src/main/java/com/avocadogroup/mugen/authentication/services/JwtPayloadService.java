package com.avocadogroup.mugen.authentication.services;

import com.avocadogroup.mugen.users.enums.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;

import javax.crypto.SecretKey;
import java.util.Date;

// This class is responsible for handling the JWT payload, it encapsulates the logic for creating and parsing JWT tokens.

@AllArgsConstructor
public class JwtPayloadService {
    private final SecretKey secretKey;
    private final Claims claims;

    // Function to check if the token is valid or expired
    public boolean isTokenExpired() {
        // Return true if the token is expired (expiration date is before the current date)
        return claims.getExpiration().before(new Date());
    }

    // Function to fetch the user id (token subject) from the token
    public Long getUserId() {
        // Return the subject which is the user id from the payload
        return Long.valueOf(claims.getSubject());
    }

    // Function to fetch the user role from the token claims
    public UserRole getUserRole() {
        // Return the user role from the bonus token claims
        return UserRole.valueOf(claims.get("role", String.class));
    }

    // Function to convert the JwtPayloadService object to a JWT token string
    public String toString() {
        return Jwts.builder() // Create a new JWT builder
                .claims(claims) // Set the claims (payload) of the token
                .signWith(secretKey) // Sign the token with the secret key
                .compact(); // Build the token and serialize it to a compact, URL-safe string
    }

}
