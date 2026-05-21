package com.ragapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ConsultantListRequest - Request lấy danh sách tư vấn viên
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request lấy danh sách tư vấn viên")
public class ConsultantListRequest {

    @Schema(
            description = "Danh mục tư vấn",
            example = "phap-ly-doanh-nghiep"
    )
    private String category;

    @Schema(
            description = "Chuyên môn tư vấn",
            example = "hợp đồng lao động"
    )
    private String specialization;

    @Schema(
            description = "Từ khóa tìm kiếm",
            example = "luật sư doanh nghiệp"
    )
    private String keyword;

    @Builder.Default
    @Schema(
            description = "Số trang",
            example = "0",
            defaultValue = "0"
    )
    private Integer pageNumber = 0;

    @Builder.Default
    @Schema(
            description = "Kích thước trang",
            example = "10",
            defaultValue = "10"
    )
    private Integer pageSize = 10;

    @Builder.Default
    @Schema(
            description = "Sắp xếp theo",
            example = "rating"
    )
    private String sortBy = "rating";
    // rating
    // completedConsultations
    // responseTime
    // newest

    @Builder.Default
    @Schema(
            description = "Sắp xếp giảm dần",
            example = "true",
            defaultValue = "true"
    )
    private Boolean descending = true;

    @Schema(
            description = "Chỉ lấy tư vấn viên online",
            example = "false"
    )
    private Boolean onlineOnly;

    @Schema(
            description = "Chỉ lấy tư vấn viên đang available",
            example = "true"
    )
    private Boolean availableOnly;
}