package com.shu.service.auth;

import com.shu.constant.JwtConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * JwtProvider is responsible for:
 * 1. Generating JWT tokens for authenticated users
 * 2. Extracting user information from JWT tokens
 *
 */
@Service
public class JwtProvider {

    // Secret key used to sign and verify JWT tokens
    private final static SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());

    /**
     * Generate a JWT token for a given authenticated user
     *
     * @param authentication Authentication object from Spring Security
     * @return JWT token as a String
     */
    public String generateToken(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities =
                authentication.getAuthorities();

        // Convert authorities into a comma-separated string
        String roles = populateAuthorities(authorities);

        return Jwts.builder()
                .issuedAt(new Date())   // current time
                .expiration(new Date(new Date().getTime() + JwtConstant.EXPIRATION_TIME))   // expiry
                .claim("email", authentication.getName())   // store email
                .claim("authorities", roles)                // store roles
                .signWith(key)                                 // sign with secret key
                .compact();
    }

    /**
     * Extract email from JWT token
     *
     * @param jwt JWT token (optionally starting with "Bearer ")
     * @return email stored in JWT claims
     */
     public String getEmailFromToken(String jwt) {
         // Remove "Bearer " prefix
         jwt = jwt.substring(7);

         // Parse and validate the JWT
         Claims claims = Jwts.parser()
                 .verifyWith(key)            // verify signature
                 .build()
                 .parseSignedClaims(jwt)     // parse JWT
                 .getPayload();              // extract claims (data inside JWT)

         // Extract email from JWT claims
         return String.valueOf(claims.get("email"));
     }

    /**
     * Convert collection of GrantedAuthority to a comma-separated string
     *
     * @param authorities list of GrantedAuthority objects
     * @return comma-separated roles string
     */
    private String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {

        Set<String> auths = new HashSet<>();
        for (GrantedAuthority authority: authorities) {
            auths.add(authority.getAuthority());
        }

        return String.join(",", auths);
    }
}
