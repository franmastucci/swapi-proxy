package com.swapi.app.proxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@ComponentScan(value = {"com.swapi"})
@SpringBootApplication
public class SwapiProxyApplication {

    public static void main(String[] args) {
        SpringApplication.run(SwapiProxyApplication.class, args);
    }

}
