package com.ragapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Mark chat as read request
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        description =
                "Request mark chat room as read"
)
public class ChatMarkAsReadRequest {

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
                    "User id is required"
    )
    @Schema(
            description =
                    "User id",
            example =
                    "user_001"
    )
    private String userId;
}
