package com.chatbot.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI chatbotOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Chatbot API")
                        .description("A Spring Boot chatbot API with conversation memory and LLM integration")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Chatbot Team")
                                .email("contact@chatbot.com")));
    }
}
