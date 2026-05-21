package com.ragapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Form suggestion
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Thông tin form được gợi ý")
public class FormSuggestion {

    @Schema(
            description = "ID form",
            example = "form_chemical_import_001"
    )
    private String formId;

    @Schema(
            description = "Mã form",
            example = "HC-02A"
    )
    private String formCode;

    @Schema(
            description = "Tên form"
    )
    private String formName;

    @Schema(
            description = "Mô tả form"
    )
    private String description;

    @Schema(
            description = "Điểm match AI",
            example = "92"
    )
    private Integer matchScore;

    @Schema(
            description = "Lý do match"
    )
    private String matchReason;

    @Schema(
            description = "Danh mục form",
            example = "chemical_import"
    )
    private String category;

    @Schema(
            description = "Loại form",
            example = "LICENSE_APPLICATION"
    )
    private String formType;

    @Schema(
            description = "Mức độ ưu tiên",
            example = "HIGH"
    )
    private String priorityLevel;

    @Schema(
            description = "Có bắt buộc không",
            example = "true"
    )
    private Boolean required;

    @Schema(
            description = "Estimated processing days",
            example = "7"
    )
    private Integer estimatedProcessingDays;

    @Schema(
            description = "Danh sách giấy tờ cần kèm theo"
    )
    private List<String> requiredDocuments;

    @Schema(
            description = "Có cần consultant hỗ trợ không",
            example = "true"
    )
    private Boolean consultantRecommended;
}