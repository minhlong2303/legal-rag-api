package com.ragapi.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;

import lombok.extern.slf4j.Slf4j;

import org.apache.http.HttpHost;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class ElasticConfig {

    @Value("${elasticsearch.host}")
    private String host;

    @Value("${elasticsearch.port}")
    private int port;

    @Value("${elasticsearch.scheme:http}")
    private String scheme;

    @Bean
    public RestClient restClient() {

        RestClientBuilder builder =
                RestClient.builder(
                        new HttpHost(
                                host,
                                port,
                                scheme
                        )
                );

        /**
         * HTTP Config
         */
        builder.setHttpClientConfigCallback(
                httpClientBuilder ->
                        httpClientBuilder
                                .setMaxConnTotal(100)
                                .setMaxConnPerRoute(20)
        );

        /**
         * Timeout Config
         */
        builder.setRequestConfigCallback(
                requestConfigBuilder ->
                        requestConfigBuilder
                                .setConnectTimeout(5000)
                                .setSocketTimeout(60000)
                                .setConnectionRequestTimeout(3000)
        );

        log.info(
                "Elasticsearch initialized: {}://{}:{}",
                scheme,
                host,
                port
        );

        return builder.build();
    }

    @Bean
    public ElasticsearchTransport elasticsearchTransport(
            RestClient restClient
    ) {

        return new RestClientTransport(
                restClient,
                new JacksonJsonpMapper()
        );
    }

    @Bean
    public ElasticsearchClient elasticsearchClient(
            ElasticsearchTransport transport
    ) {

        return new ElasticsearchClient(
                transport
        );
    }
}
