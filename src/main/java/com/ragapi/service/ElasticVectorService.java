package com.ragapi.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import dev.langchain4j.data.embedding.Embedding;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ElasticVectorService {

    private final ElasticsearchClient elasticsearchClient;
    private final EmbeddingService embeddingService;

    @Value("${elasticsearch.index}")
    private String index;

    public void indexChunk(
            String documentId,
            String content
    ) throws IOException {

        log.debug("Generating embedding for chunk of size: {}", content.length());

        Embedding embedding =
                embeddingService.generateEmbedding(content);

        Map<String, Object> data = new HashMap<>();

        data.put("documentId", documentId);
        data.put("content", content);
        data.put("vector", embedding.vector());

        IndexRequest<Map<String, Object>> request =
                IndexRequest.of(i -> i
                        .index(index)
                        .document(data)
                );

        elasticsearchClient.index(request);

        log.debug("Chunk indexed to Elasticsearch");
    }

    public List<String> search(String question)
            throws IOException {

        log.debug("Generating embedding for question");

        Embedding queryEmbedding =
                embeddingService.generateEmbedding(question);

        List<Float> queryVector = new ArrayList<>();
        for (float f : queryEmbedding.vector()) {
            queryVector.add(f);
        }

        log.debug("Performing KNN search in Elasticsearch");

        SearchResponse<Map> response =
                elasticsearchClient.search(s -> s
                                .index(index)
                                .knn(k -> k
                                        .field("vector")
                                        .queryVector(queryVector)
                                        .k(5)
                                        .numCandidates(20)
                                ),
                        Map.class
                );

        List<String> results = new ArrayList<>();

        response.hits().hits().forEach(hit -> {

            Map source = hit.source();

            if(source != null) {
                results.add(source.get("content").toString());
            }
        });

        log.info("Found {} matching chunks", results.size());

        return results;
    }
}
