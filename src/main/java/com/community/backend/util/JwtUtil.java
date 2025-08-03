package com.community.backend.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "e05348ffbb534500ab062533f412bf2462235fa6949b40bb9cec6d198474ced4"; // Use a strong key in real applications

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject(); // assuming the email is stored in the 'sub' claim
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(SECRET_KEY.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
