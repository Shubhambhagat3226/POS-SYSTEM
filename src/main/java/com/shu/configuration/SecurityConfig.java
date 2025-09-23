package com.shu.configuration;

import com.shu.filter.JwtValidator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@RequiredArgsConstructor // Use constructor injection for the filter
public class SecurityConfig {

    // Inject the JwtValidator as a Spring bean
    private final JwtValidator jwtValidator;

    /**
     *
     * In Spring Security, SecurityFilterChain decides:
     *  - Which APIs need authentication
     *  - Which APIs need special roles (like ADMIN)
     *  - Which APIs are open to the public
     *  - How tokens/sessions are handled
     *  - How CORS (Cross-Origin Requests) is managed
     *
     *
     * @param http The main Spring Security object where we configure rules
     * @return a configured SecurityFilterChain that Spring Boot will use
     * @throws Exception in case configuration fails
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http
                // 1. No session storage (we use JWT, so backend is stateless)
                .sessionManagement(management ->
                        management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 2. Define which APIs need login/roles
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/api/admin/**").hasRole("ADMIN")
                                .requestMatchers("/api/**").authenticated()
                                .anyRequest().permitAll())

                // 3. Add custom filter before Spring's BasicAuthenticationFilter
                .addFilterBefore(jwtValidator,
                        BasicAuthenticationFilter.class)

                // 4. Disable CSRF (since we’re not using session cookies)
                .csrf(AbstractHttpConfigurer::disable)

                // 5. Enable CORS (so frontend on another port/domain can call APIs)
                .cors(cors ->
                        cors.configurationSource(corsConfigurationSource()))

                // Build the final chain
                .build();
    }

    /**
     *
     * PASSWORD ENCODER:
     * Provides a PasswordEncoder for hashing user passwords securely.
     * Uses BCrypt algorithm to store and verify passwords safely.
     *
     * @return a BCryptPasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /**
     *
     * CORS CONFIGURATION:
     * CORS (Cross-Origin Resource Sharing) policy allows your frontend
     * to call your backend APIs.
     *
     * This policy specifies which external domains, methods, and headers are permitted
     * to interact with the API.
     *
     * @return A CorsConfigurationSource that provides CORS configuration per request.
     */
    private CorsConfigurationSource corsConfigurationSource() {
        return new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration cfg = new CorsConfiguration();

                //  Define the list of allowed origins frontend
                cfg.setAllowedOrigins(
                        Arrays.asList(
                                "https://localhost:5173"
                        )
                );

                // Allow all HTTP methods (GET, POST, PUT, DELETE, etc.)
                cfg.setAllowedMethods(Collections.singletonList("*"));

                // Allow credentials (cookies, authorization headers, etc.)
                cfg.setAllowCredentials(true);

                // Allow all headers (Authorization, Content-Type, etc.)
                cfg.setAllowedHeaders(Collections.singletonList("*"));

                // Expose Authorization header so frontend can read JWT tokens
                cfg.setExposedHeaders(Arrays.asList("Authorization"));

                //Expose Authorization header so frontend can read JWT tokens
                cfg.setMaxAge(3600L);

                return cfg;
            }
        };
    }
}
