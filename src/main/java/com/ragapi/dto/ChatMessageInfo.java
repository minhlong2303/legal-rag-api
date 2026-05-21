package com.ragapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Chat message detail
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        description =
                "Chat message information"
)
public class ChatMessageInfo {

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
                    "CONSULTANT"
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
    // TEXT
    // IMAGE
    // FILE
    // CONTRACT
    // PAYMENT
    // SYSTEM

    @Schema(
            description =
                    "Message sent time"
    )
    private LocalDateTime sentAt;

    @Schema(
            description =
                    "Message status",
            example =
                    "READ"
    )
    private String status;
    // SENT
    // DELIVERED
    // READ
    // DELETED

    @Schema(
            description =
                    "Attachment URL"
    )
    private String attachmentUrl;

    @Schema(
            description =
                    "Attachment name"
    )
    private String attachmentName;

    @Schema(
            description =
                    "Attachment size in bytes",
            example =
                    "204800"
    )
    private Long attachmentSize;

    @Schema(
            description =
                    "Contract id if message linked to contract",
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
                    "Message edited",
            example =
                    "false"
    )
    private Boolean edited;

    @Schema(
            description =
                    "Message edited at"
    )
    private LocalDateTime editedAt;
}
