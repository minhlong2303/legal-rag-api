package com.ragapi.service;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmbeddingService {

    private final OllamaEmbeddingModel embeddingModel;

    public Embedding generateEmbedding(String text) {

        // Validate input
        if (text == null) {
            log.error("Embedding text is null");
            throw new IllegalArgumentException(
                    "Embedding text cannot be null"
            );
        }

        // Remove extra spaces
        text = text.trim();

        // Validate empty text
        if (text.isEmpty()) {
            log.error("Embedding text is empty");
            throw new IllegalArgumentException(
                    "Embedding text cannot be empty"
            );
        }

        try {

            log.info(
                    "Generating embedding for text length: {}",
                    text.length()
            );

            Embedding embedding =
                    embeddingModel
                            .embed(text)
                            .content();

            log.info(
                    "Embedding generated successfully with dimensions: {}",
                    embedding.vector().length
            );

            return embedding;

        } catch (Exception e) {

            log.error(
                    "Failed to generate embedding: {}",
                    e.getMessage(),
                    e
            );

            throw new RuntimeException(
                    "Failed to generate embedding",
                    e
            );
        }
    }
}