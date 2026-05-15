package com.ragapi.config;

import dev.langchain4j.model.ollama.OllamaEmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAIConfig {

    // =========================
    // OPENROUTER (DeepSeek Chat)
    // =========================

    @Value("${openrouter.api-key}")
    private String openRouterApiKey;

    @Value("${openrouter.base-url}")
    private String openRouterBaseUrl;

    @Value("${openrouter.model}")
    private String openRouterModel;

    // =========================
    // OLLAMA (Embedding)
    // =========================

    @Value("${ollama.base-url}")
    private String ollamaBaseUrl;

    @Value("${ollama.embedding-model}")
    private String embeddingModel;

    // =========================
    // CHAT MODEL
    // =========================

    @Bean
    public OpenAiChatModel chatModel() {

        return OpenAiChatModel.builder()
                .apiKey(openRouterApiKey)
                .baseUrl(openRouterBaseUrl)
                .modelName(openRouterModel)
                .build();
    }

    // =========================
    // EMBEDDING MODEL
    // =========================

    @Bean
    public OllamaEmbeddingModel embeddingModel() {

        return OllamaEmbeddingModel.builder()
                .baseUrl(ollamaBaseUrl)
                .modelName(embeddingModel)
                .build();
    }
}