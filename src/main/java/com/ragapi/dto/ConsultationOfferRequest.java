package com.ragapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * ConsultationOfferRequest - Request gợi ý tư vấn viên cho user
 * (Được tạo sau khi AI trả lời)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request tạo đề xuất tư vấn")
public class ConsultationOfferRequest {

    @NotBlank(message = "User id is required")
    @Schema(
            description = "ID người dùng",
            example = "user_001"
    )
    private String userId;

    @NotBlank(message = "Question is required")
    @Schema(
            description = "Câu hỏi người dùng",
            example = "Công ty cần giấy phép gì để xử lý hóa chất?"
    )
    private String question;

    @Schema(
            description = "Câu trả lời AI"
    )
    private String aiResponse;

    @Schema(
            description = "Danh mục tư vấn",
            example = "phap-ly-doanh-nghiep"
    )
    private String category;

    @Schema(
            description = "Danh sách chuyên môn liên quan"
    )
    private List<String> specializations;

    @Schema(
            description = "Có cần gợi ý tư vấn viên hay không",
            example = "true"
    )
    @Builder.Default
    private Boolean consultationSuggested = true;

    @Schema(
            description = "Số lượng tư vấn viên muốn gợi ý",
            example = "5"
    )
    @Builder.Default
    private Integer consultantLimit = 5;
}