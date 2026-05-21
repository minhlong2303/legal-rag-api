package com.ragapi.controller;

import com.ragapi.dto.PaymentConfirmRequest;
import com.ragapi.dto.PaymentCreateRequest;
import com.ragapi.entity.Payment;
import com.ragapi.service.PaymentService;
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
@RequestMapping({"/payment", "/api/payment"})
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create")
    public ResponseEntity<?> createPayment(
            @Valid @RequestBody PaymentCreateRequest request
    ) {
        try {
            Payment payment = paymentService.createPayment(
                    request.getContractId(),
                    request.getMethod()
            );
            return ResponseEntity.ok(payment);
        } catch (Exception ex) {
            log.error("Error creating payment", ex);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    @PostMapping("/confirm")
    public ResponseEntity<?> confirmPayment(
            @Valid @RequestBody PaymentConfirmRequest request
    ) {
        return ResponseEntity
                .badRequest()
                .body(Map.of(
                        "error",
                        "Payment confirmation requires SYSTEM_ACCOUNT. Use /admin/payment/confirm/{paymentId}.",
                        "transactionId",
                        request.getTransactionId()
                ));
    }

    @GetMapping("/status/{contractId}")
    public ResponseEntity<?> getPaymentStatus(@PathVariable String contractId) {
        try {
            return ResponseEntity.ok(paymentService.getPaymentStatus(contractId));
        } catch (Exception ex) {
            log.error("Error fetching payment status", ex);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", ex.getMessage()));
        }
    }
}
