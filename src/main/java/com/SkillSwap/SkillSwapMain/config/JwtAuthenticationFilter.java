package com.SkillSwap.SkillSwapMain.config;
import com.SkillSwap.SkillSwapMain.entity.RefreshToken;
import com.SkillSwap.SkillSwapMain.entity.User;
import com.SkillSwap.SkillSwapMain.repositories.RefreshTokenRepository;
import com.SkillSwap.SkillSwapMain.repositories.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("No Bearer token found");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        try {
            String userEmail = jwtUtil.extractEmail(token);
            System.out.println("Extracted email: " + userEmail);

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
                System.out.println("UserDetails loaded: " + userDetails.getUsername() + ", Authorities: " + userDetails.getAuthorities());

                if (jwtUtil.validateToken(token)) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    System.out.println("Authentication set: " + SecurityContextHolder.getContext().getAuthentication());
                } else {
                    System.out.println("Token validation failed");
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Token");
                    return;
                }
            }
        } catch (ExpiredJwtException e) {
            // Handle expiration as before
        }

        filterChain.doFilter(request, response);
        System.out.println("Authentication after filter: " + SecurityContextHolder.getContext().getAuthentication());
    }

}