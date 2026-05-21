package com.ragapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * ConsultationOfferResponse - Response gợi ý tư vấn viên
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response gợi ý tư vấn viên")
public class ConsultationOfferResponse {

    @Schema(
            description = "ID yêu cầu tư vấn",
            example = "consultation_request_001"
    )
    private String consultationRequestId;

    @Schema(
            description = "Có nên hiển thị gợi ý tư vấn viên hay không",
            example = "true"
    )
    private Boolean shouldOfferConsultation;

    @Schema(
            description = "Danh sách tư vấn viên được gợi ý"
    )
    private List<ConsultantSuggestionDTO> suggestedConsultants;

    @Schema(
            description = "Tin nhắn hiển thị cho user",
            example = "Bạn có muốn kết nối với tư vấn viên phù hợp không?"
    )
    private String message;

    @Schema(
            description = "Tóm tắt ngắn nội dung AI đã hiểu"
    )
    private String summary;

    @Schema(
            description = "Các câu hỏi gợi ý tiếp theo"
    )
    private List<String> suggestedQuestions;

    @Schema(
            description = "Tổng số consultant match",
            example = "12"
    )
    private Integer totalMatchedConsultants;

    @Schema(
            description = "Có cho phép tạo chat ngay không",
            example = "true"
    )
    @Builder.Default
    private Boolean allowDirectChat = true;
}