/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ucan.skawallet.back.end.skawallet.security.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;

/**
 *
 * @author azm
 */
@Component
public class JwtUtil
{

//    @Value("${jwt.secret}")
    private static final SecretKey secret = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    @Value("${jwt.expiration}")
    private long expiration;

    public String generateToken(String username)
    {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String subject)
    {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public boolean validateToken(String token, String userDetails)
    {
        final String username = extractUsername(token);
        return (username.equals(userDetails) && !isTokenExpired(token));
    }

    public String extractUsername(String token)
    {
        return extractAllClaims(token).getSubject();
    }

    private Claims extractAllClaims(String token)
    {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    private boolean isTokenExpired(String token)
    {
        return extractAllClaims(token).getExpiration().before(new Date());
    }
}
