package com.shu.filter;

import com.shu.constant.JwtConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

/**
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
@Component
public class JwtValidator extends OncePerRequestFilter {

    /**
     * This method is called for every HTTP request.
     *
     * @param request  Incoming HTTP request
     * @param response Outgoing HTTP response
     * @param filterChain Chain of filters
     * @throws ServletException
     * @throws IOException
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
            } catch (Exception e) {
                // 9️. If anything fails (invalid token, expired, wrong signature), throw error
                throw new BadCredentialsException("Invalid JWT...");
            }
        }

        // Continue the filter chain (important!)
        filterChain.doFilter(request, response);
    }
}
