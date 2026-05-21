package com.ragapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Chat history response
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        description =
                "Chat history response"
)
public class ChatHistoryResponse {

    @Schema(
            description =
                    "Chat room id",
            example =
                    "chat_room_001"
    )
    private String chatRoomId;

    @Schema(
            description =
                    "Total message count",
            example = "120"
    )
    private Integer totalMessageCount;

    @Schema(
            description =
                    "Current page number",
            example = "0"
    )
    private Integer pageNumber;

    @Schema(
            description =
                    "Page size",
            example = "50"
    )
    private Integer pageSize;

    @Schema(
            description =
                    "Total pages",
            example = "3"
    )
    private Integer totalPages;

    @Schema(
            description =
                    "Has next page",
            example = "true"
    )
    private Boolean hasNext;

    @Schema(
            description =
                    "Has previous page",
            example = "false"
    )
    private Boolean hasPrevious;

    @Schema(
            description =
                    "Chat messages"
    )
    private List<ChatMessageInfo> messages;

    @Schema(
            description =
                    "Chat room status",
            example =
                    "ACTIVE"
    )
    private String roomStatus;

    @Schema(
            description =
                    "Contract id if exists",
            example =
                    "contract_001"
    )
    private String contractId;

    @Schema(
            description =
                    "Generated at"
    )
    @Builder.Default
    private LocalDateTime generatedAt =
            LocalDateTime.now();
}
