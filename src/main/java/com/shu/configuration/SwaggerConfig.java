package com.shu.configuration;

import com.shu.constant.OpenApiConstants;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SwaggerConfig
 *
 * Configures OpenAPI/Swagger documentation for the POS system.
 * This configuration defines API metadata and the available security schemes.
 * Security requirements are applied on a per-controller basis using annotations.
 */
@Configuration
public class SwaggerConfig {

    /**
     * Creates a JWT Bearer security scheme for Swagger UI.
     */
    private SecurityScheme createBearerScheme() {
        return new SecurityScheme()
                .name(OpenApiConstants.SECURITY_SCHEME_NAME)
                .type(SecurityScheme.Type.HTTP) // HTTP authentication
                .scheme("bearer")               // Bearer scheme
                .bearerFormat("JWT");           // JWT format
    }

    /**
     * OpenAPI bean configuration.
     * Adds project info, contact, license, and global JWT security.
     */
    @Bean
    public OpenAPI posOpenAPIConfig() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(OpenApiConstants.SECURITY_SCHEME_NAME,
                                createBearerScheme()))
                .info(new Info()
                        .title("POS System API")
                        .description("REST APIs for POS System including Authentication, Users, Products, Orders, and Billing")
                        .version("1.0")
                        .termsOfService("Terms of Service")
                        .contact(new Contact()
                                .name("Shubham Kumar")
                                .email("shubham@gmail.com")
                                .url("https://github.com/Shubhambhagat3226"))
                        .license(new License()
                                .name("POS API License")));
    }
}
