package com.demo.inventory.of.socks.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Inventory of socks API")
                        .version("1.0")
                        .description("REST API для учета носков на складе магазина")
                        .contact(new Contact()
                                .name("Kulakov Evgeniy")
                                .email("000evgeniykulakov@gmail.com")));
    }
}
