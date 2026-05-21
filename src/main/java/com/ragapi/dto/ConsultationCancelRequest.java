package com.ragapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ConsultationCancelRequest - Request hủy tư vấn
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request hủy yêu cầu tư vấn")
public class ConsultationCancelRequest {

    @NotBlank(message = "Consultation request id is required")
    @Schema(
            description = "ID yêu cầu tư vấn",
            example = "consultation_request_001"
    )
    private String consultationRequestId;

    @NotBlank(message = "User id is required")
    @Schema(
            description = "ID người dùng",
            example = "user_001"
    )
    private String userId;

    @Schema(
            description = "Lý do hủy",
            example = "Không còn nhu cầu tư vấn"
    )
    private String reason;

    @Schema(
            description = "Hủy từ phía frontend/app",
            example = "WEB"
    )
    @Builder.Default
    private String source = "WEB";
}