package com.ragapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "AI query response")
public class AiQueryResponse {

    @Schema(description = "AI-generated answer", example = "Nhà máy xử lý chất thải tại TP.HCM phải có giấy phép môi trường và giấy phép xây dựng...")
    private String answer;
}

