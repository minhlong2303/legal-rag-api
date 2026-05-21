package com.ragapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Metadata biểu mẫu
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Metadata của form")
public class FormMetadata {

    @Schema(
            description = "ID form",
            example = "form_001"
    )
    private String id;

    @Schema(
            description = "Mã form",
            example = "FORM-CHEM-001"
    )
    private String formCode;

    @Schema(
            description = "Tên form",
            example = "Đơn xin cấp phép hóa chất"
    )
    private String formName;

    @Schema(
            description = "Mô tả form"
    )
    private String description;

    @Schema(
            description = "Loại form",
            example = "LEGAL_FORM"
    )
    private String formType;

    @Schema(
            description = "Danh mục form",
            example = "hoa-chat"
    )
    private String category;

    @Schema(
            description = "Phiên bản form",
            example = "1.0"
    )
    private String version;

    @Schema(
            description = "Có yêu cầu chữ ký hay không",
            example = "true"
    )
    @Builder.Default
    private Boolean requiresSignature = false;

    @Schema(
            description = "Có yêu cầu thanh toán hay không",
            example = "false"
    )
    @Builder.Default
    private Boolean requiresPayment = false;

    @Schema(
            description = "Trạng thái form",
            example = "ACTIVE"
    )
    @Builder.Default
    private String status = "ACTIVE";
}