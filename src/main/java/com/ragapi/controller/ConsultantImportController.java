package com.ragapi.controller;

import com.ragapi.dto.ConsultantImportResponse;
import com.ragapi.service.ConsultantImportService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.constraints.NotNull;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/admin/consultants")
@RequiredArgsConstructor
@Tag(
        name = "Consultant Import",
        description = "Bulk import consultants from Excel"
)
public class ConsultantImportController {

    private final ConsultantImportService
            consultantImportService;

    /**
     * Import consultants
     */
    @PostMapping(
            value = "/import",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @Operation(
            summary = "Import consultants",
            description =
                    "Import consultants from Excel file"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Import success"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid file"
    )
    @ApiResponse(
            responseCode = "500",
            description = "Server error"
    )
    public ResponseEntity<?> importConsultants(

            @RequestParam("file")
            @NotNull
            @Parameter(
                    description =
                            "Excel file (.xlsx/.xls)",
                    required = true
            )
            MultipartFile file,

            @RequestParam(
                    value = "dryRun",
                    defaultValue = "false"
            )
            boolean dryRun
    ) {

        try {

            validateExcelFile(file);

            ConsultantImportResponse response =
                    consultantImportService
                            .importConsultantsFromExcel(
                                    file,
                                    dryRun
                            );

            if (Boolean.TRUE.equals(
                    response.getSuccess()
            )) {

                return ResponseEntity.ok(
                        response
                );
            }

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(response);

        } catch (IllegalArgumentException ex) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            Map.of(
                                    "success",
                                    false,
                                    "message",
                                    ex.getMessage()
                            )
                    );

        } catch (Exception ex) {

            log.error(
                    "Error importing consultants",
                    ex
            );

            return ResponseEntity
                    .status(
                            HttpStatus.INTERNAL_SERVER_ERROR
                    )
                    .body(
                            Map.of(
                                    "success",
                                    false,
                                    "message",
                                    "Internal server error"
                            )
                    );
        }
    }

    /**
     * Download template info
     */
    @GetMapping("/import/template")
    @Operation(
            summary = "Consultant import template"
    )
    public ResponseEntity<?> getTemplateInfo() {

        Map<String, String> columns =
                new LinkedHashMap<>();

        columns.put(
                "A",
                "Consultant Code"
        );

        columns.put(
                "B",
                "Full Name"
        );

        columns.put(
                "C",
                "Email"
        );

        columns.put(
                "D",
                "Phone"
        );

        columns.put(
                "E",
                "Specializations"
        );

        columns.put(
                "F",
                "Categories"
        );

        columns.put(
                "G",
                "Experience Years"
        );

        columns.put(
                "H",
                "Average Rating"
        );

        columns.put(
                "I",
                "Completed Consultations"
        );

        columns.put(
                "J",
                "Response Time"
        );

        columns.put(
                "K",
                "Max Concurrent Chats"
        );

        columns.put(
                "L",
                "Website"
        );

        columns.put(
                "M",
                "Address"
        );

        columns.put(
                "N",
                "City"
        );

        columns.put(
                "O",
                "Description"
        );

        columns.put(
                "P",
                "Status ACTIVE/INACTIVE"
        );

        return ResponseEntity.ok(
                Map.of(
                        "success",
                        true,
                        "format",
                        "xlsx",
                        "maxFileSize",
                        "5MB",
                        "columns",
                        columns,
                        "dryRun",
                        "true = validate only"
                )
        );
    }

    /**
     * Validate excel file
     */
    private void validateExcelFile(
            MultipartFile file
    ) {

        if (file == null || file.isEmpty()) {

            throw new IllegalArgumentException(
                    "File is required"
            );
        }

        String filename =
                file.getOriginalFilename();

        if (
                filename == null ||
                        (
                                !filename.endsWith(".xlsx")
                                        &&
                                        !filename.endsWith(".xls")
                        )
        ) {

            throw new IllegalArgumentException(
                    "Only Excel files are supported"
            );
        }

        String contentType =
                file.getContentType();

        if (
                contentType == null ||
                        (
                                !contentType.equals(
                                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                                )
                                        &&
                                        !contentType.equals(
                                                "application/vnd.ms-excel"
                                        )
                        )
        ) {

            throw new IllegalArgumentException(
                    "Invalid Excel file"
            );
        }

        long maxSize =
                5 * 1024 * 1024;

        if (file.getSize() > maxSize) {

            throw new IllegalArgumentException(
                    "File size exceeds 5MB"
            );
        }
    }
}
