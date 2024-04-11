package com.springboot.blog.controller;

import com.springboot.blog.payload.JwtAuthRespone;
import com.springboot.blog.payload.LoginDto;
import com.springboot.blog.payload.RegisterDto;
import com.springboot.blog.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/")
public class AuthController {
    private AuthService authService;

    @Value("${app.jwt-expiration-in-ms}")
    private int jwtExpiration;

    @Value("${app.jwt-refresh-expiration-in-ms}")
    private Long jwtRefreshExpiration;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Login")
    @PostMapping(value = {"login/", "signin/"})
    public ResponseEntity<JwtAuthRespone> login(@RequestBody LoginDto loginDto){
        JwtAuthRespone jwtAuthRespone = authService.login(loginDto);
        HttpHeaders headers = new HttpHeaders();
//        Set JWT token in response header
        headers.add(HttpHeaders.SET_COOKIE, "accessToken=" + jwtAuthRespone.getAccessToken() + "; Path=/; Max-Age=" + jwtExpiration + "; HttpOnly; SameSite=None; Secure");
        headers.add(HttpHeaders.SET_COOKIE, "refreshToken=" + jwtAuthRespone.getRefreshToken() + "; Path=/; Max-Age=" + jwtRefreshExpiration + "; HttpOnly; SameSite=None; Secure");

        return ResponseEntity.ok().headers(headers).body(jwtAuthRespone);
    }

    @Operation(summary = "Register")
    @PostMapping(value = {"register/", "signup/"})
    public ResponseEntity<String> register(@Valid @RequestBody RegisterDto registerDto){
        String response = authService.register(registerDto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Refresh Token")
    @PostMapping(value = {"refresh-token/"})
    public ResponseEntity<JwtAuthRespone> refreshToken(@CookieValue("refreshToken") String refreshToken) {
        String newAccessToken = authService.refreshAccessToken(refreshToken);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, "accessToken=" + newAccessToken + "; Path=/; Max-Age=" + jwtExpiration + "; HttpOnly; SameSite=None; Secure");

        return ResponseEntity.ok().headers(headers).body(new JwtAuthRespone(newAccessToken, null));
    }
}
