package com.ragapi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "payments")
public class Payment {

    @Id
    private String id;

    @Indexed
    private String contractId;

    @Builder.Default
    private BigDecimal amount = BigDecimal.ZERO;

    @Builder.Default
    private PaymentMethod method = PaymentMethod.BANKING;

    @Builder.Default
    private PaymentStatus status = PaymentStatus.PENDING_CONFIRMATION;

    @Indexed
    private String transactionId;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime paidAt;

    private String confirmedBySystemAccountId;

    private String confirmedBySystemAccountName;

    private LocalDateTime confirmedAt;

    private String rejectionReason;

    private LocalDateTime rejectedAt;
}
