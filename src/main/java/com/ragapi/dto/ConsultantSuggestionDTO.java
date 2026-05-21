package com.ragapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * ConsultantSuggestionDTO - Thông tin tư vấn viên được AI gợi ý
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Thông tin tư vấn viên được AI gợi ý")
public class ConsultantSuggestionDTO {

    @Schema(
            description = "ID tư vấn viên",
            example = "consultant_001"
    )
    private String id;

    @Schema(
            description = "Tên tư vấn viên",
            example = "Nguyễn Văn A"
    )
    private String consultantName;

    @Schema(
            description = "Ảnh đại diện"
    )
    private String avatarUrl;

    @Schema(
            description = "Đánh giá trung bình",
            example = "4.9"
    )
    private Double averageRating;

    @Schema(
            description = "Số lượng tư vấn đã hoàn thành",
            example = "210"
    )
    private Integer completedConsultations;

    @Schema(
            description = "Mô tả ngắn"
    )
    private String description;

    @Schema(
            description = "Điểm phù hợp AI (0-100)",
            example = "92.5"
    )
    private Double matchScore;

    @Schema(
            description = "Lý do được gợi ý"
    )
    private String matchReason;

    @Schema(
            description = "Thời gian phản hồi trung bình (phút)",
            example = "5"
    )
    private Integer responseTimeMinutes;

    @Schema(
            description = "Danh sách chuyên môn"
    )
    private List<String> specializations;

    @Schema(
            description = "Trạng thái online",
            example = "true"
    )
    @Builder.Default
    private Boolean online = false;

    @Schema(
            description = "Có thể nhận tư vấn",
            example = "true"
    )
    @Builder.Default
    private Boolean available = true;

    @Schema(
            description = "Có xác minh hệ thống",
            example = "true"
    )
    @Builder.Default
    private Boolean verified = true;
}