package br.com.beauty_book.establishment_management.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Establishment Management API")
                        .description("Service responsible for managing establishments in the BeautyBook system")
                        .version("1.0.0"));
    }
}