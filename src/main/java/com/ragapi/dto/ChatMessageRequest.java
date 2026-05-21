package com.ragapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Send chat message request
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        description =
                "Request send chat message"
)
public class ChatMessageRequest {

    @NotBlank(
            message =
                    "Chat room id is required"
    )
    @Schema(
            description =
                    "Chat room id",
            example =
                    "chat_room_001"
    )
    private String chatRoomId;

    @NotBlank(
            message =
                    "Sender id is required"
    )
    @Schema(
            description =
                    "Sender id",
            example =
                    "user_001"
    )
    private String senderId;

    @NotBlank(
            message =
                    "Sender name is required"
    )
    @Size(
            max = 255,
            message =
                    "Sender name max 255 characters"
    )
    @Schema(
            description =
                    "Sender name",
            example =
                    "Nguyễn Văn A"
    )
    private String senderName;

    @NotBlank(
            message =
                    "Sender role is required"
    )
    @Pattern(
            regexp =
                    "USER|CONSULTANT|SYSTEM",
            message =
                    "Sender role invalid"
    )
    @Schema(
            description =
                    "Sender role",
            allowableValues = {
                    "USER",
                    "CONSULTANT",
                    "SYSTEM"
            },
            example =
                    "USER"
    )
    private String senderRole;

    @Size(
            max = 5000,
            message =
                    "Content max 5000 characters"
    )
    @Schema(
            description =
                    "Message content",
            example =
                    "Tôi cần tư vấn giấy phép hóa chất"
    )
    private String content;

    @Builder.Default
    @Pattern(
            regexp =
                    "TEXT|IMAGE|FILE|CONTRACT|PAYMENT|SYSTEM",
            message =
                    "Message type invalid"
    )
    @Schema(
            description =
                    "Message type",
            allowableValues = {
                    "TEXT",
                    "IMAGE",
                    "FILE",
                    "CONTRACT",
                    "PAYMENT",
                    "SYSTEM"
            },
            example =
                    "TEXT",
            defaultValue =
                    "TEXT"
    )
    private String messageType =
            "TEXT";

    @Schema(
            description =
                    "Attachment URL"
    )
    private String attachmentUrl;

    @Schema(
            description =
                    "Attachment file name",
            example =
                    "contract.pdf"
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
}
