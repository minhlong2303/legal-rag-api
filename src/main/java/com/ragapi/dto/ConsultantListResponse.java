package com.ragapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * ConsultantListResponse - Response danh sách tư vấn viên
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response danh sách tư vấn viên")
public class ConsultantListResponse {

    @Schema(
            description = "Tổng số kết quả",
            example = "125"
    )
    private Integer totalCount;

    @Schema(
            description = "Trang hiện tại",
            example = "0"
    )
    private Integer pageNumber;

    @Schema(
            description = "Kích thước trang",
            example = "10"
    )
    private Integer pageSize;

    @Schema(
            description = "Tổng số trang",
            example = "13"
    )
    private Integer totalPages;

    @Schema(
            description = "Có trang tiếp theo hay không",
            example = "true"
    )
    private Boolean hasNext;

    @Schema(
            description = "Có trang trước hay không",
            example = "false"
    )
    private Boolean hasPrevious;

    @Schema(
            description = "Danh sách tư vấn viên"
    )
    private List<ConsultantInfo> consultants;
}