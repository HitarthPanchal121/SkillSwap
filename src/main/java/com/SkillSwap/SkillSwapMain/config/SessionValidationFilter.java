package com.SkillSwap.SkillSwapMain.config;

import com.SkillSwap.SkillSwapMain.entity.User;
import com.SkillSwap.SkillSwapMain.errorHandling.BaseResponse;
import com.SkillSwap.SkillSwapMain.repositories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Optional;

@Component
public class SessionValidationFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    public SessionValidationFilter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // List of public endpoints to bypass session validation
        String[] publicEndpoints = {"/api/auth/register", "/api/auth/login"};

        // Check if the current request matches a public endpoint
        String path = request.getRequestURI();
        boolean isPublicEndpoint = Arrays.stream(publicEndpoints).anyMatch(path::startsWith);

        if (isPublicEndpoint) {
            // Skip session validation for public endpoints
            filterChain.doFilter(request, response);
            return;
        }

        // Validate Session-ID for protected endpoints
        String sessionId = request.getHeader("Session-ID");
        String token = request.getHeader("Authorization");

        if (sessionId == null || sessionId.isEmpty()) {
            sendErrorResponse(response, BaseResponse.builder()
                    .resultCode(HttpStatus.BAD_REQUEST.value())
                    .resultMessage("Session ID is missing")
                    .build(), HttpStatus.BAD_REQUEST);
            return;
        }

        if (token == null || !token.startsWith("Bearer ")) {
            sendErrorResponse(response, BaseResponse.builder()
                    .resultCode(HttpStatus.BAD_REQUEST.value())
                    .resultMessage("Invalid or missing Authorization header")
                    .build(), HttpStatus.BAD_REQUEST);
            return;
        }

        token = token.substring(7); // Remove "Bearer " prefix

        Optional<User> userOptional = userRepository.findBySessionIdAndJwtToken(sessionId, token);

        if (userOptional.isEmpty()) {
            sendErrorResponse(response, BaseResponse.builder()
                    .resultCode(HttpStatus.UNAUTHORIZED.value())
                    .resultMessage("Invalid session or token")
                    .build(), HttpStatus.UNAUTHORIZED);
            return;
        }

        // Since sessionId is already matched in the query, no need to recheck it
        // Proceed with the filter chain
        filterChain.doFilter(request, response);
    }

    /**
     * Helper method to send a custom error response.
     *
     * @param response     The HttpServletResponse object.
     * @param baseResponse The BaseResponse object to be serialized as JSON.
     * @param status       The HTTP status code.
     * @throws IOException If an I/O error occurs.
     */
    private void sendErrorResponse(HttpServletResponse response, BaseResponse<?> baseResponse, HttpStatus status)
            throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try (PrintWriter writer = response.getWriter()) {
            com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
            writer.write(objectMapper.writeValueAsString(baseResponse));
        }
    }
}