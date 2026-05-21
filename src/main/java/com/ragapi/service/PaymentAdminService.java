package com.ragapi.service;

import com.ragapi.entity.Payment;
import com.ragapi.entity.PaymentConfirmLog;
import com.ragapi.entity.PaymentStatus;
import com.ragapi.repository.PaymentConfirmLogRepository;
import com.ragapi.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentAdminService {

    private final PaymentRepository paymentRepository;
    private final PaymentConfirmLogRepository paymentConfirmLogRepository;
    private final ContractService contractService;

    public Payment confirmPaymentBySystem(
            String paymentId,
            String systemAccountId,
            String systemAccountName
    ) {
        return confirmPaymentBySystem(
                paymentId,
                systemAccountId,
                systemAccountName,
                null
        );
    }

    public Payment confirmPaymentBySystem(
            String paymentId,
            String systemAccountId,
            String systemAccountName,
            String expectedContractId
    ) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (expectedContractId != null
                && !expectedContractId.equals(payment.getContractId())) {
            throw new RuntimeException("Payment does not belong to contract");
        }

        payment.setStatus(PaymentStatus.CONFIRMED);
        payment.setConfirmedBySystemAccountId(systemAccountId);
        payment.setConfirmedBySystemAccountName(systemAccountName);
        payment.setConfirmedAt(LocalDateTime.now());
        payment.setPaidAt(LocalDateTime.now());
        payment = paymentRepository.save(payment);

        paymentConfirmLogRepository.save(
                PaymentConfirmLog.builder()
                        .id(UUID.randomUUID().toString())
                        .paymentId(payment.getId())
                        .adminId(systemAccountId)
                        .adminName(systemAccountName)
                        .actionType("CONFIRM_PAYMENT")
                        .actionTime(LocalDateTime.now())
                        .build()
        );

        contractService.confirmPayment(payment.getContractId(), payment.getId());

        return payment;
    }
}
