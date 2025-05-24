package com.chris.shopping.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtUtil {
    private static final String SECRET_KEY = "d584bcc1202d8d1013a2dc213dc5621121dfa55b0335fab5b159c6f91dfbf764bdbf57935cc40dda09b3d04a35475e39aeb799117b217e78b8674bac96c3d15a4f02df9f19cb3c4e1455c5245a3fd4c8f02225e88e9410023a8521b4c7983dbd7b900fb5461359e31844bc9bf007902c6f4cf261160ac6e655f0e806a16cca12ff4c6fd9c381caa1f9efc15be36cf7612fede1c4a2c0a86dc85295b0729a42be7085f1d452957a808a1c82c22a221287c4edc5a308910c21ff58abca2a114ccb9225c74eea3276fd9624a7ea41d413678a1585d65c15f78a0b15341894928f2f3750de978522329fb54ec52ab576668dd294e53baf8b3bcc431ee95153993c1d";
    private static final long EXPIRATION_TIME_MS = 1000 * 60 * 30; // 30 minutes
    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        secretKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder().subject(userDetails.getUsername()) // usually the email
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS))
                .signWith(secretKey)
                .compact();
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        final Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

}
