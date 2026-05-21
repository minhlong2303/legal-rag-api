package com.ragapi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "contract_signatures")
public class ContractSignature {

    @Id
    private String id;

    @Indexed
    private String contractId;

    @Indexed
    private String userId;

    private String signerName;

    private String signatureFileId;

    private String signatureHash;

    private String ipAddress;

    @Builder.Default
    private LocalDateTime signedAt = LocalDateTime.now();

}
