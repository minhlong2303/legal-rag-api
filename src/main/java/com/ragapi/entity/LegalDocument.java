package com.ragapi.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "legal_documents")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LegalDocument {

    @Id
    private String id;

    private String title;

    private String content;

    private String category;
}
