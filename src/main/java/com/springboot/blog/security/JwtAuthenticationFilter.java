package com.springboot.blog.security;

import com.springboot.blog.exception.BadRequestException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private JwtTokenProvider tokenProvider;
    private UserDetailsService userDetailsService;

    @Value("${app.jwt-expiration-in-ms}")
    private int jwtExpiration;

    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider, UserDetailsService userDetailsService) {
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
//        Get access and refresh token from request
        String accessToken = getAccessTokenFromRequest(request);
        String refreshToken = getRefreshTokenFromRequest(request);

        String nameOfAccessToken = "Access Token";

//        Validate access token with token provider
        Pair<Boolean, String> isAccessTokenValid = tokenProvider.validateToken(accessToken, nameOfAccessToken);

        if (isAccessTokenValid.getValue0()) {
            SetAuthenticationInSecurityContext(request, accessToken);
        } else if (isAccessTokenValid.getValue1().equals(nameOfAccessToken +  " is expired")) {
            String newAccessToken = tokenProvider.refreshAccessToken(refreshToken);

            response.setHeader(HttpHeaders.SET_COOKIE, "accessToken=" + newAccessToken + "; Path=/; Max-Age=" + jwtExpiration + "; HttpOnly; SameSite=None; Secure");
        }
        else{
            throw new BadRequestException(isAccessTokenValid.getValue1());
        }

        filterChain.doFilter(request, response);
    }

    public void SetAuthenticationInSecurityContext(HttpServletRequest request,String accessToken) {
        //            Get username from JWT
        String username = tokenProvider.getUsernameFromToken(accessToken);
//            Get user from username
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

//            Create authentication object
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,
                null,
                userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//            Set authentication in SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    public String getAccessTokenFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public String getRefreshTokenFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
