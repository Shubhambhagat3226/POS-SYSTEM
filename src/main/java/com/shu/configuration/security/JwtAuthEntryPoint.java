package com.shu.configuration.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shu.payload.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * JwtAuthEntryPoint
 *
 * Handles authentication errors in JWT-based security.
 *
 * Responsibilities:
 * <ul>
 *   <li>Intercepts requests where JWT is missing, invalid, or expired.</li>
 *   <li>Returns a standardized {@link ErrorResponse} with HTTP 401 (Unauthorized).</li>
 *   <li>Works with {@link com.shu.filter.JwtValidator} to stop the filter chain for invalid requests.</li>
 *   <li>Ensures consistent error format for frontend clients.</li>
 * </ul>
 *
 * Usage:
 * <ul>
 *   <li>Automatically used by Spring Security as the {@link AuthenticationEntryPoint} for unauthorized requests.</li>
 *   <li>Called by {@link com.shu.filter.JwtValidator} whenever JWT validation fails.</li>
 * </ul>
 */
@Component
@RequiredArgsConstructor
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

    /**
     * ObjectMapper for converting {@link ErrorResponse} to JSON.
     */
    private final ObjectMapper objectMapper;

    /**
     * Called when an authentication error occurs (invalid/missing JWT).
     *
     * This method:
     * <ol>
     *   <li>Builds a structured {@link ErrorResponse} containing timestamp, status, error, message, and path.</li>
     *   <li>Sets HTTP status to 401 (Unauthorized).</li>
     *   <li>Writes the {@link ErrorResponse} to the response body as JSON.</li>
     * </ol>
     *
     * @param request the incoming HTTP request
     * @param response the outgoing HTTP response
     * @param authException the exception that triggered this entry point
     * @throws IOException if writing the response fails
     */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        // Create a structured error response
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED.value(),
                "Unauthorized",
                "Invalid or expired JWT token",
                request.getRequestURI()
        );

        // Set response properties and write the error
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        objectMapper.writeValue(response.getOutputStream(), error);
    }

}

