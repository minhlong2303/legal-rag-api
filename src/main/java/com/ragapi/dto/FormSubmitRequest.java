package com.ragapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Request submit form
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Yêu cầu submit form")
public class FormSubmitRequest {

    @NotBlank(message = "Form ID is required")
    @Schema(
            description = "ID form",
            example = "form-chemical-import-001",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String formId;

    @NotBlank(message = "User ID is required")
    @Schema(
            description = "ID người dùng",
            example = "user_001",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String userId;

    @NotEmpty(message = "Form data is required")
    @Schema(
            description = "Dữ liệu form",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Map<String, Object> formData;

    @Schema(
            description = "Lưu dưới dạng draft",
            example = "false"
    )
    @Builder.Default
    private Boolean draft = false;

    @Schema(
            description = "Có submit cho consultant review hay không",
            example = "true"
    )
    @Builder.Default
    private Boolean sendToConsultant = false;

    @Schema(
            description = "ID consultant review form",
            example = "consultant_001"
    )
    private String consultantId;

    @Schema(
            description = "Ngôn ngữ",
            example = "vi"
    )
    @Builder.Default
    private String language = "vi";
}