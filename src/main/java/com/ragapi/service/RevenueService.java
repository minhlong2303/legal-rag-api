package com.ragapi.service;

import com.ragapi.entity.PaymentStatus;
import com.ragapi.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RevenueService {

    private final PaymentRepository paymentRepository;

    public double getTotalRevenue() {
        return paymentRepository.findByStatus(PaymentStatus.CONFIRMED)
                .stream()
                .mapToDouble(payment -> 100.0)
                .sum();
    }

    public long getTotalCompletedContracts() {
        return paymentRepository.findByStatus(PaymentStatus.CONFIRMED)
                .size();
    }
}
