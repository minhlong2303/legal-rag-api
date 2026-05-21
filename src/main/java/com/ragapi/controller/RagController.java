package com.ragapi.controller;

import com.ragapi.dto.AiQueryRequest;
import com.ragapi.dto.AiQueryResponse;
import com.ragapi.dto.DocumentUploadRequest;

import com.ragapi.entity.LegalDocument;

import com.ragapi.repository.LegalDocumentRepository;

import com.ragapi.service.DocumentIngestionService;
import com.ragapi.service.PdfStorageService;
import com.ragapi.service.RagService;

import com.ragapi.util.LLMDiagnostics;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.mongodb.gridfs.GridFsResource;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(
        name = "RAG API",
        description = "Vietnamese Legal RAG API"
)
public class RagController {

    private final DocumentIngestionService
            ingestionService;

    private final RagService
            ragService;

    private final LegalDocumentRepository
            documentRepository;

    private final PdfStorageService
            pdfStorageService;

    private final LLMDiagnostics
            llmDiagnostics;

    /**
     * Upload PDF
     */
    @PostMapping(
            value = "/documents/upload",
            consumes =
                    MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @Operation(
            summary = "Upload legal PDF"
    )
    public ResponseEntity<?> uploadPdf(

            @RequestParam("file")
            MultipartFile file,

            @RequestParam("title")
            String title,

            @RequestParam(
                    value = "category",
                    defaultValue = "legal"
            )
            String category
    ) {

        try {

            validatePdfFile(file);

            LegalDocument document =
                    ingestionService.ingestPdf(
                            file,
                            title,
                            category
                    );

            return ResponseEntity.ok(
                    Map.of(
                            "status",
                            "SUCCESS",
                            "documentId",
                            document.getId(),
                            "title",
                            document.getTitle(),
                            "pdfFileId",
                            document.getPdfFileId()
                    )
            );

        } catch (IllegalArgumentException ex) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            Map.of(
                                    "error",
                                    ex.getMessage()
                            )
                    );

        } catch (Exception ex) {

            log.error(
                    "Error uploading PDF",
                    ex
            );

            return ResponseEntity
                    .status(
                            HttpStatus.INTERNAL_SERVER_ERROR
                    )
                    .body(
                            Map.of(
                                    "error",
                                    "Không tìm thấy thông tin"
                            )
                    );
        }
    }

    /**
     * Download PDF
     */
    @GetMapping("/documents/{id}/pdf")
    @Operation(
            summary = "Download original PDF"
    )
    public ResponseEntity<?> downloadPdf(

            @PathVariable String id
    ) {

        try {

            LegalDocument document =
                    documentRepository
                            .findById(id)
                            .orElseThrow(
                                    () ->
                                            new IllegalArgumentException(
                                                    "Không tìm thấy thông tin"
                                            )
                            );

            if (document.getPdfFileId() == null) {

                return ResponseEntity
                        .status(
                                HttpStatus.NOT_FOUND
                        )
                        .body(
                                Map.of(
                                        "error",
                                        "Không tìm thấy thông tin"
                                )
                        );
            }

            GridFsResource resource =
                    pdfStorageService
                            .loadByDocumentId(id);

            String fileName =
                    document.getSourceFileName()
                            != null
                            ? document.getSourceFileName()
                            : "document.pdf";

            return ResponseEntity.ok()
                    .contentType(
                            MediaType.APPLICATION_PDF
                    )
                    .header(
                            HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\""
                                    + fileName
                                    + "\""
                    )
                    .body(
                            resource
                    );

        } catch (Exception ex) {

            log.error(
                    "Error downloading PDF",
                    ex
            );

            return ResponseEntity
                    .status(
                            HttpStatus.INTERNAL_SERVER_ERROR
                    )
                    .body(
                            Map.of(
                                    "error",
                                    "Không tìm thấy thông tin"
                            )
                    );
        }
    }

    /**
     * Upload JSON document
     */
    @PostMapping("/documents")
    @Operation(
            summary = "Upload legal document JSON"
    )
    public ResponseEntity<?> uploadDocument(

            @Valid
            @RequestBody
            DocumentUploadRequest request
    ) {

        try {

            LegalDocument document =
                    new LegalDocument();

            document.setTitle(
                    request.getTitle().trim()
            );

            document.setCategory(
                    request.getCategory() == null
                            ? "legal"
                            : request.getCategory()
            );

            document.setContent(
                    request.getContent().trim()
            );

            document.setSourceType(
                    "JSON"
            );

            ingestionService.ingest(
                    document
            );

            return ResponseEntity.ok(
                    Map.of(
                            "status",
                            "SUCCESS",
                            "title",
                            document.getTitle()
                    )
            );

        } catch (Exception ex) {

            log.error(
                    "Error uploading document",
                    ex
            );

            return ResponseEntity
                    .status(
                            HttpStatus.INTERNAL_SERVER_ERROR
                    )
                    .body(
                            Map.of(
                                    "error",
                                    "Không tìm thấy thông tin"
                            )
                    );
        }
    }

    /**
     * AI legal query
     */
    @PostMapping("/ai/query")
    @Operation(
            summary = "Ask legal AI"
    )
    public ResponseEntity<?> query(

            @Valid
            @RequestBody
            AiQueryRequest request
    ) {

        try {

            if (
                    request.getQuestion() == null
                            ||
                            request.getQuestion().isBlank()
            ) {

                return ResponseEntity
                        .badRequest()
                        .body(
                                Map.of(
                                        "error",
                                        "Không tìm thấy thông tin"
                                )
                        );
            }

            String detailLevel =
                    request.getDetailLevel()
                            == null
                            ? "normal"
                            : request.getDetailLevel();

            String answer =
                    ragService.ask(
                            request.getQuestion().trim(),
                            detailLevel
                    );

            return ResponseEntity.ok(
                    AiQueryResponse.builder()
                            .answer(answer)
                            .build()
            );

        } catch (Exception ex) {

            log.error(
                    "Error querying AI",
                    ex
            );

            return ResponseEntity
                    .status(
                            HttpStatus.INTERNAL_SERVER_ERROR
                    )
                    .body(
                            Map.of(
                                    "error",
                                    "Không tìm thấy thông tin"
                            )
                    );
        }
    }

    /**
     * LLM diagnostics
     */
    @GetMapping("/health/llm")
    @Operation(
            summary = "LLM diagnostics"
    )
    public ResponseEntity<?> llmDiagnostics() {

        try {

            LLMDiagnostics.DiagnosticResult
                    result =
                    llmDiagnostics.runDiagnostics();

            return ResponseEntity.ok(
                    Map.of(
                            "overallStatus",
                            result.overallStatus,
                            "openRouterConnectivity",
                            result.openRouterConnectivity,
                            "ollamaConnectivity",
                            result.ollamaConnectivity
                    )
            );

        } catch (Exception ex) {

            log.error(
                    "Diagnostics error",
                    ex
            );

            return ResponseEntity
                    .status(
                            HttpStatus.INTERNAL_SERVER_ERROR
                    )
                    .body(
                            Map.of(
                                    "error",
                                    "Không tìm thấy thông tin"
                            )
                    );
        }
    }

    /**
     * Validate PDF
     */
    private void validatePdfFile(
            MultipartFile file
    ) {

        if (
                file == null ||
                        file.isEmpty()
        ) {

            throw new IllegalArgumentException(
                    "PDF file is required"
            );
        }

        String fileName =
                file.getOriginalFilename();

        if (
                fileName == null ||
                        !fileName.toLowerCase()
                                .endsWith(".pdf")
        ) {

            throw new IllegalArgumentException(
                    "Only PDF files are supported"
            );
        }

        if (
                file.getSize()
                        > 20 * 1024 * 1024
        ) {

            throw new IllegalArgumentException(
                    "Max PDF size is 20MB"
            );
        }
    }
}
