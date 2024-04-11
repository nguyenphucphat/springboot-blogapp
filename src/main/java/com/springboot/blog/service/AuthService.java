package com.springboot.blog.service;

import com.springboot.blog.payload.JwtAuthRespone;
import com.springboot.blog.payload.LoginDto;
import com.springboot.blog.payload.RegisterDto;

public interface AuthService {
    JwtAuthRespone login(LoginDto loginDto);

    String register(RegisterDto registerDto);

    String refreshAccessToken(String refreshToken);

}
