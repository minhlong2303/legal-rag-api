package com.ragapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ConsultantSelectionRequest - Request khi user chọn tư vấn viên
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request chọn tư vấn viên")
public class ConsultantSelectionRequest {

    @NotBlank(message = "User id is required")
    @Schema(
            description = "ID người dùng",
            example = "user_001"
    )
    private String userId;

    @NotBlank(message = "Consultation request id is required")
    @Schema(
            description = "ID yêu cầu tư vấn",
            example = "consultation_request_001"
    )
    private String consultationRequestId;

    @NotBlank(message = "Selected consultant id is required")
    @Schema(
            description = "ID tư vấn viên được chọn",
            example = "consultant_001"
    )
    private String selectedConsultantId;

    @Schema(
            description = "Ghi chú thêm của user",
            example = "Cần tư vấn gấp trong hôm nay"
    )
    private String note;
}