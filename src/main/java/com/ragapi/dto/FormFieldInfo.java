package com.ragapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Thông tin chi tiết field trong form
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Thông tin field trong form")
public class FormFieldInfo {

    @Schema(
            description = "ID field",
            example = "organizationName"
    )
    private String fieldId;

    @Schema(
            description = "Tên hiển thị",
            example = "Tên doanh nghiệp"
    )
    private String displayLabel;

    @Schema(
            description = "Kiểu field",
            example = "TEXT",
            allowableValues = {
                    "TEXT",
                    "TEXTAREA",
                    "NUMBER",
                    "DATE",
                    "SELECT",
                    "TABLE",
                    "CHECKBOX",
                    "RADIO",
                    "FILE",
                    "SIGNATURE"
            }
    )
    private String fieldType;

    @Schema(
            description = "Placeholder",
            example = "Nhập tên doanh nghiệp"
    )
    private String placeholder;

    @Schema(
            description = "Có bắt buộc hay không",
            example = "true"
    )
    private Boolean required;

    @Schema(
            description = "Danh sách gợi ý"
    )
    private List<String> suggestions;

    @Schema(
            description = "Ghi chú"
    )
    private String notes;

    @Schema(
            description = "Giá trị mặc định"
    )
    private Object defaultValue;

    @Schema(
            description = "Đã được auto-fill chưa",
            example = "true"
    )
    @Builder.Default
    private Boolean autoFilled = false;

    @Schema(
            description = "Có khóa chỉnh sửa hay không",
            example = "false"
    )
    @Builder.Default
    private Boolean readOnly = false;
}