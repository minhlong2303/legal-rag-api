package com.ragapi.config;

import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@Slf4j
public class OpenAIConfig {

    /**
     * Ollama Base URL
     */
    @Value("${ollama.base-url}")
    private String ollamaBaseUrl;

    /**
     * Chat Model
     */
    @Value("${ollama.chat-model}")
    private String chatModel;

    /**
     * Embedding Model
     */
    @Value("${ollama.embedding-model}")
    private String embeddingModel;

    /**
     * Timeout
     */
    @Value("${ollama.timeout-seconds:300}")
    private int timeoutSeconds;

    @Value("${openrouter.api-key:}")
    private String openRouterApiKey;

    @Value("${openrouter.base-url:https://openrouter.ai/api/v1}")
    private String openRouterBaseUrl;

    @Value("${openrouter.model:deepseek/deepseek-v4-flash:free}")
    private String openRouterModel;

    @Value("${openrouter.timeout-seconds:300}")
    private int openRouterTimeoutSeconds;

    @Bean
    public OpenAiChatModel openAiChatModel() {

        log.info(
                "Initializing OpenRouter chat model: {}",
                openRouterModel
        );

        String apiKey = openRouterApiKey;
        if (apiKey == null || apiKey.isBlank()) {
            log.warn(
                    "OPENROUTER_API_KEY is not configured; AI query calls will fail until it is set"
            );
            apiKey = "missing-openrouter-api-key";
        }

        return OpenAiChatModel.builder()
                .apiKey(apiKey)
                .baseUrl(openRouterBaseUrl)
                .modelName(openRouterModel)
                .timeout(
                        Duration.ofSeconds(
                                openRouterTimeoutSeconds
                        )
                )
                .build();
    }

    /**
     * Chat Model
     */
    @Bean
    public OllamaChatModel ollamaChatModel() {

        log.info(
                "Initializing Ollama chat model: {}",
                chatModel
        );

        return OllamaChatModel.builder()
                .baseUrl(ollamaBaseUrl)
                .modelName(chatModel)
                .timeout(
                        Duration.ofSeconds(
                                timeoutSeconds
                        )
                )
                .build();
    }

    /**
     * Embedding Model
     */
    @Bean
    public OllamaEmbeddingModel
    ollamaEmbeddingModel() {

        log.info(
                "Initializing embedding model: {}",
                embeddingModel
        );

        return OllamaEmbeddingModel.builder()
                .baseUrl(ollamaBaseUrl)
                .modelName(embeddingModel)
                .timeout(
                        Duration.ofSeconds(
                                timeoutSeconds
                        )
                )
                .build();
    }
}
