package com.ragapi.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ElasticHealthService
        implements CommandLineRunner {

    private final ElasticsearchClient
            elasticsearchClient;

    @Override
    public void run(String... args)
            throws Exception {

        try {
            boolean ping =
                    elasticsearchClient
                            .ping()
                            .value();

            if (ping) {
                log.info(
                        "Elasticsearch ping successful"
                );
            } else {
                log.warn(
                        "Elasticsearch ping returned false - running in degraded mode"
                );
            }
        } catch (Exception ex) {
            log.warn(
                    "Elasticsearch is unavailable - running in degraded mode: {}",
                    ex.getMessage()
            );
            log.debug(
                    "Elasticsearch health check failed",
                    ex
            );
        }
    }
}

