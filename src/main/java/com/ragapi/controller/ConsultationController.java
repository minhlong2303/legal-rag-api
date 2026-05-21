package com.ragapi.controller;

import com.ragapi.dto.*;

import com.ragapi.service.ConsultationService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/consultations")
@RequiredArgsConstructor
public class ConsultationController {

    private final ConsultationService
            consultationService;

    /**
     * Auto offer consultant
     */
    @PostMapping("/requests/{requestId}/offer")
    public ResponseEntity<?> offerConsultation(

            @PathVariable String requestId
    ) {

        try {

            ConsultationOfferResponse response =
                    consultationService
                            .offerConsultation(
                                    requestId
                            );

            return ResponseEntity.ok(
                    response
            );

        } catch (Exception ex) {

            log.error(
                    "Error offering consultation",
                    ex
            );

            return ResponseEntity
                    .status(
                            HttpStatus.INTERNAL_SERVER_ERROR
                    )
                    .body(
                            Map.of(
                                    "error",
                                    ex.getMessage()
                            )
                    );
        }
    }

    /**
     * User select consultant
     */
    @PostMapping("/select")
    public ResponseEntity<?> selectConsultant(

            @Valid
            @RequestBody
            ConsultantSelectionRequest request
    ) {

        try {

            ConsultantSelectionResponse response =
                    consultationService
                            .selectConsultant(
                                    request.getConsultationRequestId(),
                                    request.getUserId(),
                                    request.getSelectedConsultantId()
                            );

            return ResponseEntity.ok(
                    response
            );

        } catch (IllegalArgumentException ex) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            Map.of(
                                    "error",
                                    ex.getMessage()
                            )
                    );

        } catch (Exception ex) {

            log.error(
                    "Error selecting consultant",
                    ex
            );

            return ResponseEntity
                    .status(
                            HttpStatus.INTERNAL_SERVER_ERROR
                    )
                    .body(
                            Map.of(
                                    "error",
                                    "Internal server error"
                            )
                    );
        }
    }

    /**
     * Cancel consultation
     */
    @PostMapping("/cancel")
    public ResponseEntity<?> cancelConsultation(

            @Valid
            @RequestBody
            ConsultationCancelRequest request
    ) {

        try {

            consultationService
                    .cancelConsultationOffer(
                            request.getConsultationRequestId(),
                            request.getUserId(),
                            request.getReason()
                    );

            return ResponseEntity.ok(
                    Map.of(
                            "status",
                            "SUCCESS",
                            "message",
                            "Consultation cancelled"
                    )
            );

        } catch (Exception ex) {

            log.error(
                    "Error cancelling consultation",
                    ex
            );

            return ResponseEntity
                    .status(
                            HttpStatus.INTERNAL_SERVER_ERROR
                    )
                    .body(
                            Map.of(
                                    "error",
                                    ex.getMessage()
                            )
                    );
        }
    }

    /**
     * Consultation history
     */
    @GetMapping("/users/{userId}/history")
    public ResponseEntity<?> getConsultationHistory(

            @PathVariable String userId
    ) {

        try {

            var consultations =
                    consultationService
                            .getUserConsultationHistory(
                                    userId
                            );

            return ResponseEntity.ok(
                    Map.of(
                            "userId",
                            userId,
                            "count",
                            consultations.size(),
                            "consultations",
                            consultations
                    )
            );

        } catch (Exception ex) {

            log.error(
                    "Error fetching history",
                    ex
            );

            return ResponseEntity
                    .status(
                            HttpStatus.INTERNAL_SERVER_ERROR
                    )
                    .body(
                            Map.of(
                                    "error",
                                    "Internal server error"
                            )
                    );
        }
    }

    /**
     * Get consultation detail
     */
    @GetMapping("/{consultationRequestId}")
    public ResponseEntity<?> getConsultationDetail(

            @PathVariable
            String consultationRequestId
    ) {

        try {

            var consultation =
                    consultationService
                            .getConsultationDetail(
                                    consultationRequestId
                            );

            return ResponseEntity.ok(
                    consultation
            );

        } catch (Exception ex) {

            log.error(
                    "Error fetching consultation detail",
                    ex
            );

            return ResponseEntity
                    .status(
                            HttpStatus.INTERNAL_SERVER_ERROR
                    )
                    .body(
                            Map.of(
                                    "error",
                                    ex.getMessage()
                            )
                    );
        }
    }
}
