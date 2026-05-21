package com.ragapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Consultant import response
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        description =
                "Consultant import response"
)
public class ConsultantImportResponse {

    @Schema(
            description =
                    "Import success",
            example =
                    "true"
    )
    private Boolean success;

    @Schema(
            description =
                    "Summary message",
            example =
                    "Imported consultants successfully"
    )
    private String message;

    @Schema(
            description =
                    "Total Excel rows",
            example =
                    "100"
    )
    private Integer totalRows;

    @Schema(
            description =
                    "Imported rows count",
            example =
                    "95"
    )
    private Integer successCount;

    @Schema(
            description =
                    "Error rows count",
            example =
                    "5"
    )
    private Integer errorCount;

    @Schema(
            description =
                    "Imported consultant ids"
    )
    private List<String> importedConsultantIds;

    @Schema(
            description =
                    "Success detail messages"
    )
    private List<String> successMessages;

    @Schema(
            description =
                    "Error detail messages"
    )
    private List<String> errorMessages;

    @Schema(
            description =
                    "Dry run mode",
            example =
                    "false"
    )
    private Boolean dryRun;

    @Schema(
            description =
                    "Import execution time in milliseconds",
            example =
                    "1200"
    )
    private Long executionTimeMs;

    @Schema(
            description =
                    "Generated at"
    )
    @Builder.Default
    private LocalDateTime generatedAt =
            LocalDateTime.now();
}
