package com.ragapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Response chi tiết form với dữ liệu auto-fill
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Chi tiết form với dữ liệu auto-fill")
public class FormDetailResponse {

    @Schema(description = "Thông tin metadata của form")
    private FormMetadata form;

    @Schema(description = "Dữ liệu đã được tự động điền")
    private Map<String, Object> filledData;

    @Schema(description = "Các trường còn thiếu cần user nhập")
    private Map<String, FormFieldInfo> emptyFields;

    @Schema(description = "HTML/template render form")
    private String formTemplate;

    @Schema(
            description = "Có cho phép chỉnh sửa form hay không",
            example = "true"
    )
    @Builder.Default
    private Boolean editable = true;

    @Schema(
            description = "Tỷ lệ auto-fill (%)",
            example = "75"
    )
    private Integer autoFillPercentage;

    @Schema(
            description = "ID hợp đồng liên kết nếu form sinh từ chat"
    )
    private String contractId;

    @Schema(
            description = "ID phòng chat liên kết"
    )
    private String chatRoomId;

    @Schema(
            description = "Trạng thái hợp đồng",
            example = "DRAFT"
    )
    private String contractStatus;
}