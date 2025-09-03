package com.avocadogroup.mugen.authentication.services;

import com.avocadogroup.mugen.configs.JwtConfig;
import com.avocadogroup.mugen.users.User;
import com.avocadogroup.mugen.users.enums.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@AllArgsConstructor
public class JwtService {
    private final JwtConfig jwtConfig;

    // Function to generate a JWT token for a user
    private String buildJwtToken(User user, long tokenExpiration) {
        // Build and return the JWT token
        return Jwts.builder()
                .subject(String.valueOf(user.getId())) // The subject
                .claim("email", user.getEmail()) // Bonus Claims
                .claim("role", user.getRole()) // Bonus Claims
                .issuedAt(new Date()) // Token issue time
                .expiration(new Date(System.currentTimeMillis() + 1000 * tokenExpiration)) // Expiry time in milliseconds
                .signWith(Keys.hmacShaKeyFor(jwtConfig.getSecretKey().getBytes())) // Signature for security using the secret key
                .compact();
    }

    // Function to get the token claims from a JWT token (claims are the data inside the token, e.g., user ID, email, role, etc.)
    private Claims getTokenClaims(String jwtToken) {
        // Parse the JWT token and return the claims
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(jwtConfig.getSecretKey().getBytes())) // Verify the token using the secret key
                .build() // Build the parser instance after setting the verification key
                .parseSignedClaims(jwtToken) // Parse the signed JWT token
                .getPayload(); // Get the claims (data) from the token
    }

    // Function to generate JWT tokens
    public String generateAccessToken(User user) {
        final long tokenExpiration = jwtConfig.getAccessTokenExpirationTime(); // Access token expiration time in seconds

        // Build Json Web token using the jwt builder
        return buildJwtToken(user, tokenExpiration);
    }

    // Function to generate refresh tokens
    public String generateRefreshToken(User user) {
        final long tokenExpiration = jwtConfig.getRefreshTokenExpirationTime(); // Refresh token expiration time in seconds

        // Build Json Web token using the jwt builder
        return buildJwtToken(user, tokenExpiration);
    }

    // Function to check if the token is expired
    public boolean isTokenExpired(String jwtToken) {
        try {
            // Parse the token and extract the claims (payload)
            var claims = getTokenClaims(jwtToken);

            // Return true if the token is expired (expiration is before now)
            return claims.getExpiration().before(new Date());
        } catch (JwtException e) {
            // If parsing the token fails, consider it expired
            return true;
        }
    }

    // Function to fetch the user id from the token subject
    public Long getUserIdFromToken(String jwtToken) {
        // Parse the token and extract the claims (payload)
        var claims = getTokenClaims(jwtToken);

        // Return the subject which is the user id from the claim
        return Long.valueOf(claims.getSubject());
    }

    // Function to fetch the user role from the token claims
    public UserRole getUserRoleFromToken(String jwtToken) {
        // Parse the token and extract the claims (payload)
        var claims = getTokenClaims(jwtToken);

        // Return the user role from the token claims (as an enum)
        return UserRole.valueOf(claims.get("role", String.class));
    }

}
