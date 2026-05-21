package com.ragapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractPaymentRequest {

    @NotBlank(message = "Payment id is required")
    private String paymentId;

    @NotBlank(message = "System account id is required")
    private String systemAccountId;

    @NotBlank(message = "System account name is required")
    private String systemAccountName;
}
