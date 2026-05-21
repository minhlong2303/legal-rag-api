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
public class SignRequest {

    @NotBlank(message = "Contract id is required")
    private String contractId;

    @NotBlank(message = "User id is required")
    private String userId;

    @NotBlank(message = "Signer name is required")
    private String signerName;

    private String signatureImage;

    private String ipAddress;
}
