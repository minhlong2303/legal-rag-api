package com.ragapi.controller;

import com.ragapi.dto.ContractPaymentRequest;
import com.ragapi.entity.Contract;
import com.ragapi.service.ContractService;
import com.ragapi.service.PaymentAdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping({"/contract", "/api/contract"})
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;
    private final PaymentAdminService paymentAdminService;

    @PostMapping("/create/{chatRoomId}")
    public ResponseEntity<?> createContract(@PathVariable String chatRoomId) {
        try {
            Contract contract = contractService.createContractFromChat(chatRoomId);
            return ResponseEntity.ok(contract);
        } catch (Exception ex) {
            log.error("Error creating contract", ex);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    @PostMapping("/pay/{contractId}")
    public ResponseEntity<?> confirmPayment(
            @PathVariable String contractId,
            @Valid @RequestBody ContractPaymentRequest request
    ) {
        try {
            paymentAdminService.confirmPaymentBySystem(
                    request.getPaymentId(),
                    request.getSystemAccountId(),
                    request.getSystemAccountName(),
                    contractId
            );

            return ResponseEntity.ok(contractService.getContract(contractId));
        } catch (Exception ex) {
            log.error("Error confirming contract payment", ex);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    @PostMapping("/complete/{contractId}")
    public ResponseEntity<?> completeContract(@PathVariable String contractId) {
        try {
            return ResponseEntity.ok(contractService.completeContract(contractId));
        } catch (Exception ex) {
            log.error("Error completing contract", ex);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getContract(@PathVariable String id) {
        try {
            return ResponseEntity.ok(contractService.getContract(id));
        } catch (Exception ex) {
            log.error("Error fetching contract", ex);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", ex.getMessage()));
        }
    }
}
