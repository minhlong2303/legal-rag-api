package com.ragapi.controller;

import com.ragapi.dto.AiQueryRequest;
import com.ragapi.dto.AiQueryResponse;
import com.ragapi.dto.DocumentUploadRequest;
import com.ragapi.entity.LegalDocument;
import com.ragapi.service.DocumentIngestionService;
import com.ragapi.service.RagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "RAG API", description = "Retrieval-Augmented Generation APIs")
public class RagController {

    private final DocumentIngestionService ingestionService;
    private final RagService ragService;

    @PostMapping("/documents")
    @Operation(summary = "Upload legal document for indexing")
    public ResponseEntity<?> uploadDocument(
            @RequestBody DocumentUploadRequest request
    ) {
        try {
            log.info("Uploading document: {}", request.getTitle());

            // Convert DTO to entity
            LegalDocument document = new LegalDocument();
            document.setTitle(request.getTitle());
            document.setCategory(request.getCategory());
            document.setContent(request.getContent());

            ingestionService.ingest(document);
            log.info("Document uploaded successfully: {}", document.getId());
            return ResponseEntity.ok("Document indexed successfully");
        } catch (IOException e) {
            log.error("Error during document ingestion", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Elasticsearch error: " + e.getMessage()));
        } catch (Exception e) {
            log.error("Unexpected error during document upload", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Server error: " + e.getMessage()));
        }
    }

    @PostMapping("/ai/query")
    @Operation(summary = "Query AI with RAG retrieval")
    public ResponseEntity<?> query(
            @RequestBody AiQueryRequest request
    ) {
        try {
            String question = request.getQuestion();
            if (question == null || question.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Question is required"));
            }

            log.info("Processing question: {}", question);
            String answer = ragService.ask(question);
            log.info("Answer generated successfully");

            AiQueryResponse response = new AiQueryResponse(answer);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            log.error("Error during AI query - Elasticsearch error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Elasticsearch error: " + e.getMessage()));
        } catch (Exception e) {
            log.error("Unexpected error during AI query", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Server error: " + e.getMessage()));
        }
    }
}
