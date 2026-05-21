package com.ragapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Chat closure response
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        description =
                "Chat room closure response"
)
public class ChatClosureResponse {

    @Schema(
            description =
                    "Chat room id",
            example =
                    "chat_room_001"
    )
    private String chatRoomId;

    @Schema(
            description =
                    "Response status",
            example =
                    "SUCCESS"
    )
    private String status;

    @Schema(
            description =
                    "Response message",
            example =
                    "Chat room closed successfully"
    )
    private String message;

    @Schema(
            description =
                    "Chat closed time"
    )
    private LocalDateTime closedAt;

    @Schema(
            description =
                    "Generated contract id",
            example =
                    "contract_001"
    )
    private String contractId;

    @Schema(
            description =
                    "Contract generated",
            example =
                    "true"
    )
    private Boolean contractGenerated;

    @Schema(
            description =
                    "Deal summary from consultation",
            example =
                    "Tư vấn giấy phép hóa chất"
    )
    private String dealSummary;

    @Schema(
            description =
                    "Need payment confirmation",
            example =
                    "true"
    )
    private Boolean paymentRequired;

    @Schema(
            description =
                    "Contract status",
            example =
                    "WAITING_PAYMENT"
    )
    private String contractStatus;
}
