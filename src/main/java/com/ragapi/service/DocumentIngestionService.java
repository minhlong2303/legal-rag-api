package com.ragapi.service;

import com.ragapi.entity.LegalDocument;
import com.ragapi.repository.LegalDocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentIngestionService {

    private final LegalDocumentRepository repository;
    private final TextChunkingService chunkingService;
    private final ElasticVectorService vectorService;

    public void ingest(LegalDocument doc)
            throws IOException {

        log.info("Starting ingestion for document: {}", doc.getTitle());

        // Save to MongoDB
        repository.save(doc);
        log.info("Document saved to MongoDB with id: {}", doc.getId());

        // Chunk content
        List<String> chunks =
                chunkingService.chunk(
                        doc.getContent(),
                        500
                );
        log.info("Document chunked into {} chunks", chunks.size());

        // Index chunks to Elasticsearch
        for(String chunk : chunks) {

            vectorService.indexChunk(
                    doc.getId(),
                    chunk
            );
        }

        log.info("All chunks indexed to Elasticsearch");
    }
}
