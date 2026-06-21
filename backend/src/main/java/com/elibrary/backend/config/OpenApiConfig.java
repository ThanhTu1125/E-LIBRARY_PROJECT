package com.elibrary.backend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("E-Library Web3 & AI API")
                        .version("1.0")
                        .description(
                                "Tài liệu API tự động cho hệ thống Thư viện số E-Library. Tích hợp AI Recommendation và lõi Blockchain phân tán."))
                // Đính kèm yêu cầu bảo mật (Cái ổ khóa) vào toàn bộ hệ thống
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                // Định nghĩa cách thức hoạt động của cái ổ khóa (Dùng Bearer Token)
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}