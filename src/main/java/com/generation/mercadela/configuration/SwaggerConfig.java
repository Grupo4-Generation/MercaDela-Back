package com.generation.mercadela.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SwaggerConfig {

    @Bean
    OpenAPI springMercadelaOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Mercadela")
                        .description("Projeto MercaDela - Generation Brasil")
                        .version("v0.0.1")
                        .license(new License()
                                .name("Generation Brasil")
                                .url("https://brazil.generation.org"))
                        .contact(new Contact()
                                .name("MercaDela")
                                .url("https://github.com/Grupo4-Generation/MercaDela")
                                .email("merca_dela@outlook.com")))
                .externalDocs(new ExternalDocumentation()
                        .description("Github")
                        .url("https://github.com/Grupo4-Generation/MercaDela"));
    }

}
