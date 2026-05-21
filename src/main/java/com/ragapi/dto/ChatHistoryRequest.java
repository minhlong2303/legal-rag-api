package com.ragapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Chat history request
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        description =
                "Request get chat history"
)
public class ChatHistoryRequest {

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

    @Min(
            value = 0,
            message =
                    "Page number must be >= 0"
    )
    @Builder.Default
    @Schema(
            description =
                    "Page number",
            example = "0",
            defaultValue = "0"
    )
    private Integer pageNumber = 0;

    @Min(
            value = 1,
            message =
                    "Page size must be >= 1"
    )
    @Max(
            value = 100,
            message =
                    "Page size max is 100"
    )
    @Builder.Default
    @Schema(
            description =
                    "Messages per page",
            example = "50",
            defaultValue = "50"
    )
    private Integer pageSize = 50;

    @Builder.Default
    @Schema(
            description =
                    "Include deleted messages",
            example = "false",
            defaultValue = "false"
    )
    private Boolean includeDeleted = false;
}

