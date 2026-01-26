package com.company.leave.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;

@Component
public class JwtUtil {

    private static final String SECRET =
            "my-secret-key-my-secret-key-my-secret-key"; // ≥ 32 chars

    private final Key key =
            Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Long extractEmployeeId(String token) {
        return extractClaims(token).get("employeeId", Long.class);
    }

    public String extractRole(String token) {
        return extractClaims(token).get("role", String.class);
    }
}
