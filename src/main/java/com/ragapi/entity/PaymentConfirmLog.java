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
@Document(collection = "payment_confirm_logs")
public class PaymentConfirmLog {

    @Id
    private String id;

    @Indexed
    private String paymentId;

    @Indexed
    private String adminId;

    private String adminName;

    @Builder.Default
    private LocalDateTime actionTime = LocalDateTime.now();

    private String actionType;
}
