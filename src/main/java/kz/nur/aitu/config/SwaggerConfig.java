package kz.nur.aitu.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("AITU Student Platform API")
                        .version("1.0")
                        .description("API для управления студентческими клубами, событиями и новостями в Astana IT University")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }
}
