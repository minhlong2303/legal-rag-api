package com.ragapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Document upload request")
public class DocumentUploadRequest {

    @Schema(description = "Document title", example = "Waste Treatment Regulation")
    private String title;

    @Schema(description = "Document category", example = "environment")
    private String category;

    @Schema(description = "Document content", example = "Nhà máy xử lý chất thải tại TP.HCM phải có giấy phép môi trường...")
    private String content;
}

