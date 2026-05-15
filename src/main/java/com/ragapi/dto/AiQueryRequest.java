package com.ragapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "AI query request")
public class AiQueryRequest {

    @Schema(description = "Question to ask AI", example = "Nhà máy xử lý chất thải cần giấy phép gì?")
    private String question;
}

