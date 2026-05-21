package com.ragapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Response submit form
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Kết quả submit form")
public class FormSubmitResponse {

    @Schema(
            description = "Trạng thái xử lý",
            example = "SUCCESS"
    )
    private String status;
    // SUCCESS
    // VALIDATION_ERROR
    // DRAFT_SAVED
    // CONSULTANT_REVIEW_REQUIRED
    // ERROR

    @Schema(
            description = "Thông báo kết quả"
    )
    private String message;

    @Schema(
            description = "ID form đã submit",
            example = "submitted_form_001"
    )
    private String submittedFormId;

    @Schema(
            description = "Validation errors"
    )
    private Map<String, String> validationErrors;

    @Schema(
            description = "Bước tiếp theo cho người dùng"
    )
    private String nextSteps;

    @Schema(
            description = "Đã gửi consultant review chưa",
            example = "true"
    )
    private Boolean sentToConsultant;

    @Schema(
            description = "ID consultant xử lý"
    )
    private String consultantId;

    @Schema(
            description = "Có cần thanh toán không",
            example = "false"
    )
    private Boolean paymentRequired;

    @Schema(
            description = "Mã hợp đồng nếu có"
    )
    private String contractId;

    @Schema(
            description = "Thời gian submit"
    )
    private LocalDateTime submittedAt;
}