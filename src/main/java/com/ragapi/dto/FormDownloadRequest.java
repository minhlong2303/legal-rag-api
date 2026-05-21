package com.ragapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Request download form đã điền
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Yêu cầu tải xuống form")
public class FormDownloadRequest {

    @NotBlank(message = "Form id is required")
    @Schema(
            description = "ID biểu mẫu",
            example = "form-02a-import-banned-chemicals",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String formId;

    @NotBlank(message = "User id is required")
    @Schema(
            description = "ID người dùng",
            example = "user_001",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String userId;

    @NotNull(message = "Form data is required")
    @Schema(
            description = "Dữ liệu form đã điền",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Map<String, Object> formData;

    @NotBlank(message = "Format is required")
    @Schema(
            description = "Định dạng file",
            example = "PDF",
            allowableValues = {"PDF", "EXCEL"},
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String format;

    @Schema(
            description = "ID hợp đồng liên kết"
    )
    private String contractId;

    @Schema(
            description = "Có chữ ký điện tử hay không",
            example = "true"
    )
    @Builder.Default
    private Boolean includeSignature = true;

    @Schema(
            description = "Có xuất file cuối cùng hay file nháp",
            example = "FINAL"
    )
    @Builder.Default
    private String exportMode = "FINAL";
}