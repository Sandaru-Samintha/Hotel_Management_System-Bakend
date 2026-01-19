package com.example.Hotel_Management_System.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Service
public class JWTUtils {

                                                                    // Token validity time (7 days)
    public static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7;

                                                                    // Secret key used to sign and verify JWT
    private final SecretKey key;

                                                                    // Constructor: creates secret key for JWT signing
    public JWTUtils() {
                                                                    // Secret string (should be strong and private)
        String secretString = "K98as7d9as8d7as9d8as7d9as8d7as9d8as7d9as8d7as9d8as7d9as";

                                                                    // Convert secret string to bytes
        byte[] keyBytes = Base64.getDecoder()
                .decode(secretString.getBytes(StandardCharsets.UTF_8));

                                                                    // Create HMAC SHA256 secret key
        this.key = new SecretKeySpec(keyBytes, "HmacSHA256");
    }

                                                                    // Generate JWT token using username
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())              // Store username in token
                .setIssuedAt(new Date())                            // Token creation time
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Expiry time
                .signWith(key)                                      // Sign token with secret key
                .compact();                                         // Build token
    }

                                                                    // Extract username from JWT token
    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

                                                                    // Generic method to extract any claim from token
    private <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {

                                                                    // Parse token and verify signature
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

                                                                    // Return required claim
        return claimsResolver.apply(claims);
    }

                                                                    // Validate token: check username and expiration
    public boolean isValidToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);

                                                                    // Token is valid if username matches and token not expired
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

                                                                    // Check whether token is expired
    private boolean isTokenExpired(String token) {
        return extractClaims(token, Claims::getExpiration)
                .before(new Date());
    }
}
