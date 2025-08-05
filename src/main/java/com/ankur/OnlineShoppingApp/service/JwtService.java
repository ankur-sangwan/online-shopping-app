package com.ankur.OnlineShoppingApp.service;

import com.ankur.OnlineShoppingApp.model.Users;
import com.ankur.OnlineShoppingApp.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.io.Decoders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Value;

import java.util.Map;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;



@Service
public class JwtService {
    @Value("${jwt.secretKey}")
    private String secretKey;
    @Autowired
    private UserRepository userRepository;

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        // Agar extra claims add karni hain to yahan karo

        Users user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        claims.put("role", user.getRole().name());
        claims.put("userId", user.getId());
        return Jwts.builder()
                .claims(claims)  // Extra data include krta hai (role, userId).
                .subject(username) // Token kis user ke liye hai.
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30)) // 30 minutes in milliseconds
                .signWith(getKey())
                .compact();

    }

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUserName(String token) {
        return extractAllClaims(token).getSubject();
    }

    //  Validates token: username matches AND token is not expired
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUserName(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    //  Checks if token is expired
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    //  Extracts expiration date
    private Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    //  Extracts all claims from token
    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public int extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        return (Integer) claims.get("userId");
    }

}