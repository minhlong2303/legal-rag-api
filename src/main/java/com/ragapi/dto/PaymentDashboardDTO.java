package com.ragapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDashboardDTO {

    private String paymentId;

    private String contractId;

    private String userId;

    private String consultantId;

    private String contractTitle;

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime confirmedAt;

    private String confirmedBy;
}
