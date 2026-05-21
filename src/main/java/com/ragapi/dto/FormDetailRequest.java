package com.ragapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request lấy chi tiết form và auto-fill dữ liệu
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Yêu cầu lấy chi tiết form và auto-fill dữ liệu")
public class FormDetailRequest {

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

    @Builder.Default
    @Schema(
            description = "Tự động điền dữ liệu từ profile",
            example = "true"
    )
    private Boolean autoFill = true;

    @Schema(
            description = "ID hợp đồng liên kết nếu form được tạo từ chat chốt hợp đồng",
            example = "contract_001"
    )
    private String contractId;

    @Schema(
            description = "ID phòng chat liên quan",
            example = "chat_room_001"
    )
    private String chatRoomId;
}