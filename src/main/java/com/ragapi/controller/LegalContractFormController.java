package com.ragapi.controller;

import com.ragapi.entity.LegalContractForm;
import com.ragapi.entity.LegalContractFormStatus;
import com.ragapi.service.LegalContractFormService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping({"/legal/form", "/api/legal/form"})
@RequiredArgsConstructor
public class LegalContractFormController {

    private final LegalContractFormService service;

    @GetMapping("/generate/{chatRoomId}")
    public ResponseEntity<?> generate(
            @PathVariable String chatRoomId,
            @RequestParam String contractId
    ) {
        try {
            LegalContractForm form = service.generateFromChat(chatRoomId, contractId);
            return ResponseEntity.ok(form);
        } catch (Exception ex) {
            log.error("Error generating legal contract form", ex);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    @GetMapping("/{formId}")
    public ResponseEntity<?> getById(@PathVariable String formId) {
        try {
            return ResponseEntity.ok(service.getById(formId));
        } catch (Exception ex) {
            log.error("Error fetching legal contract form", ex);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    @GetMapping("/contract/{contractId}")
    public ResponseEntity<?> getByContractId(@PathVariable String contractId) {
        try {
            return ResponseEntity.ok(service.getByContractId(contractId));
        } catch (Exception ex) {
            log.error("Error fetching legal contract form by contract", ex);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    @PostMapping("/{formId}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable String formId,
            @RequestParam LegalContractFormStatus status
    ) {
        try {
            return ResponseEntity.ok(service.updateStatus(formId, status));
        } catch (Exception ex) {
            log.error("Error updating legal contract form status", ex);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", ex.getMessage()));
        }
    }
}
