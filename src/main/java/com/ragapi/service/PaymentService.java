package com.ragapi.service;

import com.ragapi.entity.Contract;
import com.ragapi.entity.Payment;
import com.ragapi.entity.PaymentMethod;
import com.ragapi.entity.PaymentStatus;
import com.ragapi.repository.ContractRepository;
import com.ragapi.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ContractRepository contractRepository;
    private final ContractService contractService;

    public Payment createPayment(String contractId) {
        return createPayment(contractId, PaymentMethod.BANKING);
    }

    public Payment createPayment(String contractId, PaymentMethod method) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new RuntimeException("Contract not found"));

        var existingPayment = paymentRepository.findByContractId(contractId);
        if (existingPayment.isPresent()
                && PaymentStatus.CONFIRMED.equals(existingPayment.get().getStatus())) {
            return existingPayment.get();
        }

        if (existingPayment.isPresent()
                && PaymentStatus.PENDING_CONFIRMATION.equals(existingPayment.get().getStatus())) {
            return existingPayment.get();
        }

        Payment payment = Payment.builder()
                .id(UUID.randomUUID().toString())
                .contractId(contractId)
                .amount(BigDecimal.valueOf(100.0))
                .method(method != null ? method : PaymentMethod.BANKING)
                .status(PaymentStatus.PENDING_CONFIRMATION)
                .transactionId("TXN-" + UUID.randomUUID())
                .createdAt(LocalDateTime.now())
                .build();

        payment = paymentRepository.save(payment);
        contractService.markPaymentPending(contractId, payment.getId());

        return payment;
    }

    public Payment confirmPayment(String transactionId) {
        return paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new RuntimeException(
                        "Payment is waiting for SYSTEM_ACCOUNT confirmation"
                ));
    }

    public Payment getPaymentStatus(String contractId) {
        return paymentRepository.findByContractId(contractId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
    }
}
