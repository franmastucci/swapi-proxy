package com.swapi.app.proxy.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI api() {
        return new OpenAPI().info(new Info()
                .title("trileuco-swapi-proxy")
                        .description("<h4>This API acts as a proxy for consuming "
                                + "the SWAPI (Star Wars API). <br> It provides an abstraction layer to access different"
                                + " types of data related to the Star Wars universe.</h4>")
                .version("Spring Boot 3.1.2")
                .contact(new Contact()
                        .name("Francisco Mastucci Silva")
                        .email("franmastucci@gmail.com")
                        .url("https://github.com/franmastucci/swapi-proxy")));
    }

}

