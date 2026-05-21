package com.ragapi.repository;

import com.ragapi.entity.LegalDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LegalDocumentRepository
        extends MongoRepository<LegalDocument, String> {
}
