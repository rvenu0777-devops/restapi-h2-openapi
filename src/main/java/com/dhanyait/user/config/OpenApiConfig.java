package com.dhanyait.user.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI userApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("User API")
                        .description("Spring Boot 3 + H2 + OpenAPI example")
                        .version("1.0.0"));
    }
}
