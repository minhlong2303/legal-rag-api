package com.ragapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Chat closure request
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        description =
                "Request close consultation chat"
)
public class ChatClosureRequest {

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

    @DecimalMin(
            value = "1.0",
            message =
                    "Rating must be >= 1"
    )
    @DecimalMax(
            value = "5.0",
            message =
                    "Rating must be <= 5"
    )
    @Schema(
            description =
                    "User rating 1-5",
            example = "5"
    )
    private Double userRating;

    @Size(
            max = 2000,
            message =
                    "Feedback max 2000 characters"
    )
    @Schema(
            description =
                    "User feedback",
            example =
                    "Tư vấn nhanh và dễ hiểu"
    )
    private String userFeedback;

    @Schema(
            description =
                    "Deal summary after consultation",
            example =
                    "Tư vấn giấy phép hóa chất"
    )
    private String dealSummary;

    @Schema(
            description =
                    "Generate contract after close",
            example =
                    "true"
    )
    @Builder.Default
    private Boolean generateContract =
            false;
}
