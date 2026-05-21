package com.ragapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ConsultantSelectionResponse - Response khi user chọn tư vấn viên
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response sau khi chọn tư vấn viên")
public class ConsultantSelectionResponse {

    @Schema(
            description = "ID phòng chat được tạo",
            example = "chat_room_001"
    )
    private String chatRoomId;

    @Schema(
            description = "ID tư vấn viên",
            example = "consultant_001"
    )
    private String consultantId;

    @Schema(
            description = "Tên tư vấn viên",
            example = "Nguyễn Văn A"
    )
    private String consultantName;

    @Schema(
            description = "Email tư vấn viên"
    )
    private String consultantEmail;

    @Schema(
            description = "Ảnh đại diện tư vấn viên"
    )
    private String consultantAvatarUrl;

    @Schema(
            description = "Trạng thái phòng chat",
            example = "ACTIVE"
    )
    private String chatRoomStatus;

    @Schema(
            description = "Tin nhắn phản hồi",
            example = "Đã tạo phòng chat thành công"
    )
    private String message;

    @Schema(
            description = "Cho phép gửi tin nhắn",
            example = "true"
    )
    private Boolean canChat;

    @Schema(
            description = "Có yêu cầu thanh toán trước hay không",
            example = "false"
    )
    private Boolean paymentRequired;
}