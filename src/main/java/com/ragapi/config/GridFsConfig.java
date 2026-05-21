package com.ragapi.config;

import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

@Configuration
@Slf4j
public class GridFsConfig {

    public static final String CONTRACT_BUCKET =
            "contract_pdfs";

    public static final String RECEIPT_BUCKET =
            "payment_receipts";

    public static final String SIGNATURE_BUCKET =
            "signatures";

    public static final String LEGAL_DOC_BUCKET =
            "legal_documents";

    @Bean
    public GridFsTemplate contractGridFsTemplate(
            MongoDatabaseFactory databaseFactory,
            MongoConverter mongoConverter
    ) {

        log.info(
                "Initializing contract GridFS bucket"
        );

        return new GridFsTemplate(
                databaseFactory,
                mongoConverter,
                CONTRACT_BUCKET
        );
    }

    @Bean
    public GridFsTemplate receiptGridFsTemplate(
            MongoDatabaseFactory databaseFactory,
            MongoConverter mongoConverter
    ) {

        return new GridFsTemplate(
                databaseFactory,
                mongoConverter,
                RECEIPT_BUCKET
        );
    }

    @Bean
    public GridFsTemplate signatureGridFsTemplate(
            MongoDatabaseFactory databaseFactory,
            MongoConverter mongoConverter
    ) {

        return new GridFsTemplate(
                databaseFactory,
                mongoConverter,
                SIGNATURE_BUCKET
        );
    }

    @Bean
    public GridFsTemplate legalDocumentGridFsTemplate(
            MongoDatabaseFactory databaseFactory,
            MongoConverter mongoConverter
    ) {

        return new GridFsTemplate(
                databaseFactory,
                mongoConverter,
                LEGAL_DOC_BUCKET
        );
    }
}
