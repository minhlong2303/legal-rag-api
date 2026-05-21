package com.ragapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * ConsultantInfo - Thông tin consultant
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Thông tin tư vấn viên")
public class ConsultantInfo {

    @Schema(description = "ID tư vấn viên", example = "consultant_001")
    private String id;

    @Schema(description = "Mã tư vấn viên", example = "LS001")
    private String consultantCode;

    @Schema(description = "Tên tư vấn viên", example = "Nguyễn Văn A")
    private String consultantName;

    @Schema(description = "Ảnh đại diện")
    private String avatarUrl;

    @Schema(description = "Mô tả ngắn")
    private String description;

    @Schema(description = "Danh sách chuyên môn")
    private List<String> specializations;

    @Schema(description = "Đánh giá trung bình", example = "4.8")
    private Double averageRating;

    @Schema(description = "Số lượng tư vấn đã hoàn thành", example = "120")
    private Integer completedConsultations;

    @Schema(description = "Thời gian phản hồi trung bình (phút)", example = "10")
    private Integer responseTimeMinutes;

    @Schema(description = "Email")
    private String email;

    @Schema(description = "Số điện thoại")
    private String phone;

    @Schema(description = "Trạng thái hoạt động", example = "true")
    @Builder.Default
    private Boolean active = true;

    @Schema(description = "Đang online", example = "false")
    @Builder.Default
    private Boolean online = false;

    @Schema(description = "Có thể nhận tư vấn", example = "true")
    @Builder.Default
    private Boolean availableForConsultation = true;
}