package com.example.DietPlanBackend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class GroqConfig {

    @Value("${groq.api.key}")
    private String groqApiKey;

    @Bean
    public WebClient groqWebClient() {
        return WebClient.builder()
                .baseUrl("https://api.groq.com/openai/v1") // Groq's OpenAI-compatible endpoint
                .defaultHeader("Authorization", "Bearer " + groqApiKey)
                .build();
    }
}
