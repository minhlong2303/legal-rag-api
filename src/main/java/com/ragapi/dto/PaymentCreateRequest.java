package com.ragapi.dto;

import com.ragapi.entity.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCreateRequest {

    @NotBlank(message = "Contract id is required")
    private String contractId;

    private PaymentMethod method;
}
