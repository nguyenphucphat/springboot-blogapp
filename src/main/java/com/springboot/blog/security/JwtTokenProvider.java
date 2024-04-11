package com.springboot.blog.security;

import com.springboot.blog.entity.User;
import com.springboot.blog.exception.BadRequestException;
import com.springboot.blog.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${app.jwt-secret}")
    private String jwtSecret;

    @Value("${app.jwt-expiration-in-ms}")
    private Long jwtExpiration;

    @Value("${app.jwt-refresh-expiration-in-ms}")
    private Long jwtRefreshExpiration;

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;

    public JwtTokenProvider(AuthenticationManager authenticationManager, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }

    private Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    // Generate JWT Access Token
    public String generateAccessToken(Authentication authentication){
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

    // Generate JWT Refresh Token
    public String generateRefreshToken(Authentication authentication){
        String username = authentication.getName();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtRefreshExpiration);

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
    public Pair<Boolean,String> validateToken(String token, String nameOfToken){
        try {
            Jwts.parser()
                    .verifyWith((SecretKey) key())
                    .build()
                    .parse(token);

            return new Pair<>(true, null);
        } catch (MalformedJwtException malformedJwtException){
            return new Pair<>(false, "Invalid " + nameOfToken);
        } catch (ExpiredJwtException expiredJwtException){
            return new Pair<>(false, nameOfToken + " is expired");
        } catch (UnsupportedJwtException unsupportedJwtException){
            return new Pair<>(false, "Unsupported " + nameOfToken);
        } catch (IllegalArgumentException illegalArgumentException) {
            return new Pair<>(false, nameOfToken + " claims string is empty or null");
        }
    }

    public String refreshAccessToken(String refreshToken){
        Pair<Boolean,String> isRefreshTokeValid = validateToken(refreshToken, "Refresh Token");
        if (!isRefreshTokeValid.getValue0()) {
            throw new BadRequestException(isRefreshTokeValid.getValue1());
        }

        String username = getUsernameFromToken(refreshToken);

        User user = userRepository.findByUsername(username).orElseThrow(() -> new BadRequestException("User not found with Refresh Token"));

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        String newAccessToken = generateAccessToken(authentication);

        return newAccessToken;
    }
}
