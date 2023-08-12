package com.swapi.app.proxy.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI api() {
        return new OpenAPI().info(new Info()
                .title("swapi-trileuco-proxy")
                .version("Spring Boot 3.1.2"));
    }

}

