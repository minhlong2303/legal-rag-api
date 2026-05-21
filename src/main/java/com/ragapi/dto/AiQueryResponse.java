package com.ragapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        description =
                "AI legal query response"
)
public class AiQueryResponse {

    @Schema(
            description =
                    "AI generated answer",
            example =
                    "Nhà máy xử lý chất thải cần giấy phép môi trường."
    )
    private String answer;

    @Schema(
            description =
                    "Response status",
            example =
                    "SUCCESS"
    )
    @Builder.Default
    private String status =
            "SUCCESS";

    @Schema(
            description =
                    "Suggested follow-up questions"
    )
    private List<String> suggestedQuestions;

    @Schema(
            description =
                    "AI conversation summary"
    )
    private String summary;

    @Schema(
            description =
                    "Consultation suggested",
            example =
                    "true"
    )
    private Boolean consultationSuggested;

    @Schema(
            description =
                    "Conversation id",
            example =
                    "conversation_001"
    )
    private String conversationId;

    @Schema(
            description =
                    "Processing timestamp"
    )
    @Builder.Default
    private LocalDateTime timestamp =
            LocalDateTime.now();
}

