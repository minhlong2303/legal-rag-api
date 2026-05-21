package com.ragapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        description =
                "Vietnamese legal AI query request"
)
public class AiQueryRequest {

    @NotBlank(
            message =
                    "Question is required"
    )
    @Size(
            min = 5,
            max = 3000,
            message =
                    "Question must be between 5 and 3000 characters"
    )
    @Schema(
            description =
                    "Vietnamese legal question",
            example =
                    "Nhà máy xử lý chất thải cần giấy phép gì?"
    )
    private String question;

    @Pattern(
            regexp =
                    "brief|normal|detailed",
            message =
                    "detailLevel must be: brief, normal, detailed"
    )
    @Schema(
            description =
                    "Response detail level",
            example = "normal",
            allowableValues = {
                    "brief",
                    "normal",
                    "detailed"
            },
            defaultValue = "normal"
    )
    @Builder.Default
    private String detailLevel =
            "normal";

    @Schema(
            description =
                    "Optional conversation id",
            example =
                    "conversation_123"
    )
    private String conversationId;

    @Schema(
            description =
                    "Optional user id",
            example =
                    "user_001"
    )
    private String userId;
}
