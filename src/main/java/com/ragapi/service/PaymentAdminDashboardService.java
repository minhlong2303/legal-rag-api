package com.ragapi.service;

import com.ragapi.dto.PaymentDashboardDTO;
import com.ragapi.entity.Contract;
import com.ragapi.entity.Payment;
import com.ragapi.entity.PaymentConfirmLog;
import com.ragapi.entity.PaymentStatus;
import com.ragapi.repository.ContractRepository;
import com.ragapi.repository.PaymentConfirmLogRepository;
import com.ragapi.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentAdminDashboardService {

    private final PaymentRepository paymentRepository;
    private final ContractRepository contractRepository;
    private final PaymentConfirmLogRepository paymentConfirmLogRepository;
    private final ContractService contractService;

    public List<PaymentDashboardDTO> getPendingPayments() {
        return paymentRepository.findByStatus(PaymentStatus.PENDING_CONFIRMATION)
                .stream()
                .map(this::toDashboardDto)
                .toList();
    }

    public Payment confirmPayment(String paymentId, String adminId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.setStatus(PaymentStatus.CONFIRMED);
        payment.setConfirmedBySystemAccountId(adminId);
        payment.setConfirmedBySystemAccountName(adminId);
        payment.setConfirmedAt(LocalDateTime.now());
        payment.setPaidAt(LocalDateTime.now());
        payment = paymentRepository.save(payment);

        paymentConfirmLogRepository.save(
                PaymentConfirmLog.builder()
                        .id(UUID.randomUUID().toString())
                        .paymentId(payment.getId())
                        .adminId(adminId)
                        .adminName(adminId)
                        .actionType("CONFIRM_PAYMENT")
                        .actionTime(LocalDateTime.now())
                        .build()
        );

        contractService.confirmPayment(payment.getContractId(), payment.getId());

        return payment;
    }

    public Payment rejectPayment(String paymentId, String adminId, String reason) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.setStatus(PaymentStatus.REJECTED);
        payment.setConfirmedBySystemAccountId(adminId);
        payment.setConfirmedBySystemAccountName(adminId);
        payment.setConfirmedAt(LocalDateTime.now());
        payment.setRejectionReason(reason);
        payment.setRejectedAt(LocalDateTime.now());
        payment = paymentRepository.save(payment);

        paymentConfirmLogRepository.save(
                PaymentConfirmLog.builder()
                        .id(UUID.randomUUID().toString())
                        .paymentId(payment.getId())
                        .adminId(adminId)
                        .adminName(adminId)
                        .actionType("REJECT_PAYMENT")
                        .actionTime(LocalDateTime.now())
                        .build()
        );

        contractRepository.findById(payment.getContractId())
                .ifPresent(contract -> {
                    contract.setPaymentStatus(PaymentStatus.REJECTED);
                    contract.setUpdatedAt(LocalDateTime.now());
                    contractRepository.save(contract);
                });

        return payment;
    }

    private PaymentDashboardDTO toDashboardDto(Payment payment) {
        Contract contract = contractRepository.findById(payment.getContractId())
                .orElse(null);

        return PaymentDashboardDTO.builder()
                .paymentId(payment.getId())
                .contractId(payment.getContractId())
                .contractTitle(contract != null ? contract.getTitle() : "N/A")
                .userId(contract != null ? contract.getUserId() : null)
                .consultantId(contract != null ? contract.getConsultantId() : null)
                .status(payment.getStatus() != null ? payment.getStatus().name() : null)
                .createdAt(payment.getCreatedAt())
                .confirmedAt(payment.getConfirmedAt())
                .confirmedBy(payment.getConfirmedBySystemAccountName())
                .build();
    }
}
