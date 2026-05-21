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
@Document(collection = "contracts")
public class Contract {

    @Id
    private String id;

    @Indexed
    private String consultationRequestId;

    @Indexed
    private String chatRoomId;

    @Indexed
    private String userId;

    @Indexed
    private String consultantId;

    private String title;

    private String content;

    @Builder.Default
    private ContractStatus status = ContractStatus.DRAFT;

    @Builder.Default
    private PaymentStatus paymentStatus = PaymentStatus.INITIATED;

    private String pdfFileId;

    private String pdfUrl;

    @Builder.Default
    private Boolean signed = false;

    private LocalDateTime signedAt;

    private String signedByUserId;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
}
