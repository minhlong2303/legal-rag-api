package com.ragapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Response kết quả tìm form phù hợp
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Kết quả tìm form phù hợp")
public class FormSearchResponse {

    @Schema(
            description = "Câu hỏi người dùng"
    )
    private String question;

    @Schema(
            description = "Danh sách form được gợi ý"
    )
    private List<FormSuggestion> suggestions;

    @Schema(
            description = "Giải thích AI"
    )
    private String explanation;

    @Schema(
            description = "Tóm tắt yêu cầu người dùng"
    )
    private String summary;

    @Schema(
            description = "Các câu hỏi gợi ý tiếp theo"
    )
    private List<String> suggestedQuestions;

    @Schema(
            description = "Có cần tư vấn viên hay không",
            example = "true"
    )
    @Builder.Default
    private Boolean consultationRecommended = false;

    @Schema(
            description = "Tổng số form match",
            example = "3"
    )
    private Integer totalMatchedForms;
}