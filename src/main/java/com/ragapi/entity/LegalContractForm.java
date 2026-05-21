package com.ragapi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "legal_contract_forms")
public class LegalContractForm {

    @Id
    private String id;

    @Indexed
    private String contractId;

    @Indexed
    private String chatRoomId;

    @Indexed
    private String consultationRequestId;

    private LegalContractFormType formType;

    private String title;

    private String content;

    private List<FormField> fields;

    @Builder.Default
    private LegalContractFormStatus status = LegalContractFormStatus.GENERATED;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
}
