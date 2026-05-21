package com.ragapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Chat room detail response
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        description =
                "Chat room detail response"
)
public class ChatRoomDetailResponse {

    @Schema(
            description =
                    "Chat room id",
            example =
                    "chat_room_001"
    )
    private String chatRoomId;

    @Schema(
            description =
                    "User id",
            example =
                    "user_001"
    )
    private String userId;

    @Schema(
            description =
                    "User name",
            example =
                    "Nguyễn Văn A"
    )
    private String userName;

    @Schema(
            description =
                    "Consultant id",
            example =
                    "consultant_001"
    )
    private String consultantId;

    @Schema(
            description =
                    "Consultant name",
            example =
                    "Luật sư Trần Văn B"
    )
    private String consultantName;

    @Schema(
            description =
                    "Consultant email"
    )
    private String consultantEmail;

    @Schema(
            description =
                    "Consultant avatar URL"
    )
    private String consultantAvatarUrl;

    @Schema(
            description =
                    "Original AI question"
    )
    private String originalQuestion;

    @Schema(
            description =
                    "Original AI response"
    )
    private String aiResponse;

    @Schema(
            description =
                    "Chat room status",
            example =
                    "ACTIVE"
    )
    private String status;
    // ACTIVE
    // WAITING_PAYMENT
    // WAITING_SIGNATURE
    // CONTRACT_SIGNED
    // CLOSED
    // EXPIRED

    @Schema(
            description =
                    "Total message count",
            example = "25"
    )
    private Integer messageCount;

    @Schema(
            description =
                    "Chat room created at"
    )
    private LocalDateTime createdAt;

    @Schema(
            description =
                    "Last message time"
    )
    private LocalDateTime lastMessageAt;

    @Schema(
            description =
                    "Unread for current user",
            example =
                    "true"
    )
    private Boolean isUnread;

    @Schema(
            description =
                    "Contract id if generated",
            example =
                    "contract_001"
    )
    private String contractId;

    @Schema(
            description =
                    "Contract status",
            example =
                    "WAITING_PAYMENT"
    )
    private String contractStatus;

    @Schema(
            description =
                    "Payment confirmed",
            example =
                    "false"
    )
    private Boolean paymentConfirmed;

    @Schema(
            description =
                    "Contract signed",
            example =
                    "false"
    )
    private Boolean signed;

    @Schema(
            description =
                    "Deal summary from chat"
    )
    private String dealSummary;

    @Schema(
            description =
                    "Can send message",
            example =
                    "true"
    )
    private Boolean canSendMessage;

    @Schema(
            description =
                    "Chat expires at"
    )
    private LocalDateTime expiresAt;
}
