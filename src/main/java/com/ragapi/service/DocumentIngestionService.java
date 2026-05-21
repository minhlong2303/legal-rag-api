package com.ragapi.service;

import com.ragapi.entity.LegalDocument;
import com.ragapi.repository.LegalDocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentIngestionService {

    private final LegalDocumentRepository repository;
    private final VietnameseLegalChunkingService chunkingService;
    private final ElasticVectorService vectorService;
    private final PdfExtractionService pdfExtractionService;
    private final PdfStorageService pdfStorageService;

    public void ingest(LegalDocument doc) throws IOException {

        log.info("Starting ingestion for document: {}", doc.getTitle());

        repository.save(doc);
        log.info("Document saved to MongoDB with id: {}", doc.getId());

        indexContent(doc.getId(), doc.getContent());
    }

    public LegalDocument ingestPdf(
            MultipartFile file,
            String title,
            String category
    ) throws IOException {

        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Tiêu đề văn bản không được để trống");
        }

        byte[] pdfBytes = file.getBytes();
        String content = pdfExtractionService.extractText(pdfBytes, file.getOriginalFilename());

        LegalDocument doc = new LegalDocument();
        doc.setTitle(title.trim());
        doc.setCategory(category != null && !category.isBlank() ? category.trim() : "phap-luat");
        doc.setContent(content);
        doc.setSourceFileName(file.getOriginalFilename());
        doc.setSourceType("PDF");
        doc.setPdfFileSize((long) pdfBytes.length);

        repository.save(doc);
        log.info("Document saved to MongoDB with id: {}", doc.getId());

        String pdfFileId = pdfStorageService.store(
                pdfBytes,
                file.getOriginalFilename(),
                doc.getId()
        );
        doc.setPdfFileId(pdfFileId);
        repository.save(doc);
        log.info("PDF file stored in GridFS with id: {}", pdfFileId);

        indexContent(doc.getId(), doc.getContent());
        return doc;
    }

    private void indexContent(String documentId, String content) throws IOException {

        List<String> chunks = chunkingService.chunk(content);
        log.info("Document chunked into {} chunks", chunks.size());

        for (String chunk : chunks) {
            vectorService.indexChunk(documentId, chunk);
        }

        log.info("All chunks indexed to Elasticsearch");
    }
}
