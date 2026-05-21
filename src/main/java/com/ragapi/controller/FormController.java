package com.ragapi.controller;

import com.ragapi.dto.*;

import com.ragapi.service.FormAutoFillService;
import com.ragapi.service.FormExportService;
import com.ragapi.service.FormMatcherService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/forms")
@RequiredArgsConstructor
@Tag(
        name = "Form Management",
        description = "Legal chemical forms"
)
public class FormController {

    private final FormMatcherService
            formMatcherService;

    private final FormAutoFillService
            formAutoFillService;

    private final FormExportService
            formExportService;

    /**
     * Search forms
     */
    @PostMapping("/search")
    @Operation(
            summary = "Search matching forms"
    )
    public ResponseEntity<?> searchForms(

            @Valid
            @RequestBody
            FormSearchRequest request
    ) {

        try {

            FormSearchResponse response =
                    formMatcherService
                            .matchQuestionToForms(
                                    request.getQuestion(),
                                    request.getPurpose()
                            );

            return ResponseEntity.ok(
                    response
            );

        } catch (Exception ex) {

            log.error(
                    "Error searching forms",
                    ex
            );

            return ResponseEntity
                    .badRequest()
                    .body(
                            Map.of(
                                    "error",
                                    "Không tìm thấy thông tin"
                            )
                    );
        }
    }

    /**
     * Get form detail
     */
    @PostMapping("/detail")
    @Operation(
            summary =
                    "Get form detail with autofill"
    )
    public ResponseEntity<?> getFormDetail(

            @Valid
            @RequestBody
            FormDetailRequest request
    ) {

        try {

            FormDetailResponse response =
                    formAutoFillService
                            .getFormDetailWithAutoFill(
                                    request.getFormId(),
                                    request.getUserId()
                            );

            return ResponseEntity.ok(
                    response
            );

        } catch (Exception ex) {

            log.error(
                    "Error getting form detail",
                    ex
            );

            return ResponseEntity
                    .badRequest()
                    .body(
                            Map.of(
                                    "error",
                                    "Không tìm thấy thông tin"
                            )
                    );
        }
    }

    /**
     * Submit form
     */
    @PostMapping("/submit")
    @Operation(
            summary = "Submit form"
    )
    public ResponseEntity<?> submitForm(

            @Valid
            @RequestBody
            FormSubmitRequest request
    ) {

        try {

            FormSubmitResponse response =
                    formAutoFillService
                            .submitForm(
                                    request
                            );

            return ResponseEntity.ok(
                    response
            );

        } catch (Exception ex) {

            log.error(
                    "Error submitting form",
                    ex
            );

            return ResponseEntity
                    .badRequest()
                    .body(
                            Map.of(
                                    "error",
                                    "Không tìm thấy thông tin"
                            )
                    );
        }
    }

    /**
     * Update profile
     */
    @PostMapping("/users/{userId}/profile")
    @Operation(
            summary =
                    "Update autofill profile"
    )
    public ResponseEntity<?> updateUserProfile(

            @PathVariable String userId,

            @RequestBody
            Map<String, Object> profileData
    ) {

        try {

            formAutoFillService
                    .updateUserProfile(
                            userId,
                            profileData
                    );

            return ResponseEntity.ok(
                    Map.of(
                            "status",
                            "SUCCESS"
                    )
            );

        } catch (Exception ex) {

            log.error(
                    "Error updating profile",
                    ex
            );

            return ResponseEntity
                    .badRequest()
                    .body(
                            Map.of(
                                    "error",
                                    "Không tìm thấy thông tin"
                            )
                    );
        }
    }

    /**
     * Get all forms
     */
    @GetMapping
    @Operation(
            summary = "Get all forms"
    )
    public ResponseEntity<?> getAllForms() {

        try {

            var forms =
                    formMatcherService
                            .getAllAvailableForms();

            return ResponseEntity.ok(
                    Map.of(
                            "count",
                            forms.size(),
                            "forms",
                            forms
                    )
            );

        } catch (Exception ex) {

            log.error(
                    "Error fetching forms",
                    ex
            );

            return ResponseEntity
                    .badRequest()
                    .body(
                            Map.of(
                                    "error",
                                    "Không tìm thấy thông tin"
                            )
                    );
        }
    }

    /**
     * Download form
     */
    @PostMapping("/download")
    @Operation(
            summary =
                    "Download form PDF/EXCEL"
    )
    public ResponseEntity<?> downloadForm(

            @Valid
            @RequestBody
            FormDownloadRequest request
    ) {

        try {

            String format =
                    request.getFormat() == null
                            ? "PDF"
                            : request.getFormat()
                            .toUpperCase();

            byte[] fileContent;

            String contentType;

            String extension;

            if ("EXCEL".equals(format)) {

                fileContent =
                        formExportService
                                .exportToExcel(
                                        request.getFormId(),
                                        request.getFormData()
                                );

                contentType =
                        "application/vnd.ms-excel";

                extension = "xlsx";

            } else {

                fileContent =
                        formExportService
                                .exportToPDF(
                                        request.getFormId(),
                                        request.getFormData()
                                );

                contentType =
                        MediaType.APPLICATION_PDF_VALUE;

                extension = "pdf";
            }

            String fileName =
                    formExportService
                            .generateFileName(
                                    request.getFormId(),
                                    extension
                            );

            return ResponseEntity.ok()
                    .header(
                            HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\""
                                    + fileName
                                    + "\""
                    )
                    .contentType(
                            MediaType.parseMediaType(
                                    contentType
                            )
                    )
                    .body(
                            fileContent
                    );

        } catch (Exception ex) {

            log.error(
                    "Error downloading form",
                    ex
            );

            return ResponseEntity
                    .status(
                            HttpStatus.BAD_REQUEST
                    )
                    .body(
                            Map.of(
                                    "error",
                                    "Không tìm thấy thông tin"
                            )
                    );
        }
    }
}
