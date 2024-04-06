package com.springboot.blog.security;

import com.springboot.blog.exception.BadRequestException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.UnsupportedKeyException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${app.jwt-secret}")
    private String jwtSecret;

    @Value("${app.jwt-expiration-in-ms}")
    private int jwtExpiration;

    private Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    // Generate JWT token
    public String generateToken(Authentication authentication){
        String username = authentication.getName();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        String token = Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(expiryDate)
                .signWith(key())
                .compact();

        return token;
    }

    // Get username from JWT token
    public String getUsernameFromToken(String token){
        String username = Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();

        return username;
    }

    // Validate JWT token
    public boolean validateToken(String token){
        try {
            Jwts.parser()
                    .verifyWith((SecretKey) key())
                    .build()
                    .parse(token);

            return true;
        } catch (MalformedJwtException malformedJwtException){
            throw new BadRequestException("Invalid JWT token");
        } catch (ExpiredJwtException expiredJwtException){
            throw new BadRequestException("Expired JWT token");
        } catch (UnsupportedJwtException unsupportedJwtException){
            throw new BadRequestException("Unsupported JWT token");
        } catch (IllegalArgumentException illegalArgumentException){
            throw new BadRequestException("JWT claims string is empty or null");
        }
    }
}
