package com.ragapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Upload văn bản dạng JSON
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Upload văn bản pháp lý hoặc tài liệu")
public class DocumentUploadRequest {

    @NotBlank(message = "Title is required")
    @Schema(
            description = "Tiêu đề tài liệu",
            example = "Luật Bảo vệ môi trường 2020"
    )
    private String title;

    @Builder.Default
    @Schema(
            description = "Danh mục tài liệu",
            example = "phap-luat"
    )
    private String category = "phap-luat";

    @NotBlank(message = "Content is required")
    @Schema(
            description = "Nội dung tài liệu",
            example = "Điều 1. Phạm vi điều chỉnh..."
    )
    private String content;

    @Builder.Default
    @Schema(
            description = "Nguồn dữ liệu",
            example = "MANUAL"
    )
    private String sourceType = "MANUAL";

    @Schema(
            description = "Tên file gốc nếu có",
            example = "luat-moi-truong.txt"
    )
    private String sourceFileName;

    @Schema(
            description = "Người upload",
            example = "admin_001"
    )
    private String uploadedBy;

    @Builder.Default
    @Schema(
            description = "Ngôn ngữ tài liệu",
            example = "vi"
    )
    private String language = "vi";
}