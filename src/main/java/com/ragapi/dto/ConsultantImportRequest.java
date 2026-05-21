package com.ragapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Consultant import request
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        description =
                "Request import consultants from Excel"
)
public class ConsultantImportRequest {

    @Builder.Default
    @Schema(
            description =
                    "Dry run mode. true = validate only, false = save to database",
            example =
                    "false",
            defaultValue =
                    "false"
    )
    private Boolean dryRun = false;

    @Schema(
            description =
                    "Uploaded by admin user id",
            example =
                    "admin_001"
    )
    private String uploadedBy;

    @Schema(
            description =
                    "Import source",
            example =
                    "EXCEL"
    )
    @Builder.Default
    private String importSource =
            "EXCEL";
}
