package com.ragapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Chat message response
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        description =
                "Chat message response"
)
public class ChatMessageResponse {

    @Schema(
            description =
                    "Message id",
            example =
                    "msg_001"
    )
    private String messageId;

    @Schema(
            description =
                    "Chat room id",
            example =
                    "chat_room_001"
    )
    private String chatRoomId;

    @Schema(
            description =
                    "Sender id",
            example =
                    "user_001"
    )
    private String senderId;

    @Schema(
            description =
                    "Sender name",
            example =
                    "Nguyễn Văn A"
    )
    private String senderName;

    @Schema(
            description =
                    "Sender role",
            example =
                    "USER"
    )
    private String senderRole;

    @Schema(
            description =
                    "Sender avatar URL"
    )
    private String senderAvatarUrl;

    @Schema(
            description =
                    "Message content",
            example =
                    "Tôi cần tư vấn giấy phép hóa chất"
    )
    private String content;

    @Schema(
            description =
                    "Message type",
            example =
                    "TEXT"
    )
    private String messageType;

    @Schema(
            description =
                    "Message sent time"
    )
    private LocalDateTime sentAt;

    @Schema(
            description =
                    "Message status",
            example =
                    "SENT"
    )
    private String status;
    // SENT
    // DELIVERED
    // READ

    @Schema(
            description =
                    "Attachment URL"
    )
    private String attachmentUrl;

    @Schema(
            description =
                    "Attachment file name"
    )
    private String attachmentName;

    @Schema(
            description =
                    "Contract id if related",
            example =
                    "contract_001"
    )
    private String contractId;

    @Schema(
            description =
                    "Payment confirmation id",
            example =
                    "payment_001"
    )
    private String paymentConfirmationId;

    @Schema(
            description =
                    "System generated message",
            example =
                    "false"
    )
    private Boolean systemMessage;

    @Schema(
            description =
                    "Need refresh contract UI",
            example =
                    "true"
    )
    private Boolean refreshContractUi;
}
