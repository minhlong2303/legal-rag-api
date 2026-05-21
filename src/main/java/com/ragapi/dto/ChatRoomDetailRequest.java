package com.ragapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Chat room detail request
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        description =
                "Request get chat room detail"
)
public class ChatRoomDetailRequest {

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

    @Schema(
            description =
                    "Current user id",
            example =
                    "user_001"
    )
    private String userId;
}
