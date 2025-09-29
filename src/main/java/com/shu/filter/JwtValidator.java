package com.shu.filter;

import com.shu.configuration.security.JwtAuthEntryPoint;
import com.shu.constant.JwtConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;

/*
 * JwtValidator is a Spring Security filter that checks incoming HTTP requests
 * for a valid JWT (JSON Web Token).
 *
 * Think of this filter as a "gatekeeper" that:
 *  1. Looks at the Authorization header
 *  2. Extracts the JWT
 *  3. Validates it
 *  4. Extracts user info and roles
 *  5. Sets authentication for Spring Security
 *
 * This filter runs **once per request** (extends OncePerRequestFilter)
 * to ensure efficiency.
 */
/**
 * JwtValidator
 *
 * A Spring Security filter that validates JWT tokens in HTTP requests.
 *
 * Responsibilities:
 * <ul>
 *   <li>Extracts JWT from Authorization header.</li>
 *   <li>Validates JWT signature and expiration.</li>
 *   <li>Extracts user details (email, roles) from token claims.</li>
 *   <li>Sets Spring Security context with authenticated user.</li>
 *   <li>Delegates failed authentication to {@link JwtAuthEntryPoint}.</li>
 * </ul>
 *
 * Usage:
 * <ul>
 *   <li>Automatically invoked once per request by Spring Security.</li>
 *   <li>Must be registered in {@link com.shu.configuration.security.SecurityConfig} before
 *       {@link org.springframework.security.web.authentication.www.BasicAuthenticationFilter}.</li>
 * </ul>
 */
@Component
@RequiredArgsConstructor
public class JwtValidator extends OncePerRequestFilter {

    private final JwtAuthEntryPoint jwtAuthEntryPoint;

    /**
     * This method is called for every HTTP request.
     *
     * @param request  Incoming HTTP request
     * @param response Outgoing HTTP response
     * @param filterChain the chain of filters
     * @throws ServletException in case of servlet errors
     * @throws IOException in case of I/O errors
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {


        // 1. Get the JWT token from the Authorization header
        String jwt = request.getHeader(JwtConstant.AUTH_HEADER);

        // 2. Check if JWT is present and starts with "Bearer "
        if(jwt != null && jwt.startsWith(JwtConstant.BEARER_PREFIX)) {
            // Remove "Bearer " prefix to get the actual token
            jwt = jwt.substring(7);

            try {
                // 3. Convert the secret key string into a SecretKey object
                SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());

                // 4. Parse and validate the JWT
                Claims claims = Jwts.parser()
                        .verifyWith(key)            // verify signature
                        .build()
                        .parseSignedClaims(jwt)     // parse JWT
                        .getPayload();              // extract claims (data inside JWT)

                // 5. Extract email and authorities (roles) from JWT claims
                String email = String.valueOf(claims.get("email"));
                String authorities = String.valueOf(claims.get("authorities"));

                // 6️. Convert roles from String to List<GrantedAuthority>
                List<GrantedAuthority> auths = AuthorityUtils
                        .commaSeparatedStringToAuthorityList(authorities);

                // 7️. Create Spring Security authentication object
                Authentication auth = new UsernamePasswordAuthenticationToken(email, null, auths);

                // 8️. Set authentication in the SecurityContext
                // Spring Security will now consider this user as "authenticated"
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (ExpiredJwtException ex) {
                // 👇 Call entrypoint instead of throwing
                jwtAuthEntryPoint.commence(
                        request, response,
                        new BadCredentialsException("JWT token expired", ex)
                );
                return; // stop filter chain
            } catch (JwtException ex) {
                jwtAuthEntryPoint.commence(
                        request, response,
                        new BadCredentialsException("Invalid JWT token", ex)
                );
                return;
            }
        }

        // Continue the filter chain (important!)
        filterChain.doFilter(request, response);
    }
}
