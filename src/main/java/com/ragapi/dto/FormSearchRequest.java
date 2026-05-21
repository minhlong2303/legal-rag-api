package com.ragapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request tìm form phù hợp
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Yêu cầu tìm form phù hợp")
public class FormSearchRequest {

    @NotBlank(message = "Question is required")
    @Schema(
            description = "Câu hỏi người dùng",
            example = "Tôi muốn nhập khẩu hóa chất cấm, cần giấy phép gì?",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String question;

    @Schema(
            description = "ID người dùng",
            example = "user_001"
    )
    private String userId;

    @Schema(
            description = "Mục đích xử lý",
            example = "import"
    )
    private String purpose;

    @Schema(
            description = "Danh mục pháp lý",
            example = "hoa-chat"
    )
    private String category;

    @Schema(
            description = "Có cần AI gợi ý form hay không",
            example = "true"
    )
    @Builder.Default
    private Boolean useAiMatching = true;

    @Schema(
            description = "Số lượng form muốn lấy",
            example = "5"
    )
    @Builder.Default
    private Integer limit = 5;

    @Schema(
            description = "Ngôn ngữ",
            example = "vi"
    )
    @Builder.Default
    private String language = "vi";
}