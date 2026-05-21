package com.ragapi.controller;

import com.ragapi.dto.PaymentDashboardDTO;
import com.ragapi.entity.Payment;
import com.ragapi.service.PaymentAdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping({"/admin/dashboard/payment", "/api/admin/dashboard/payment"})
@RequiredArgsConstructor
public class PaymentDashboardController {

    private final PaymentAdminDashboardService service;

    @GetMapping("/pending")
    public List<PaymentDashboardDTO> getPending() {
        return service.getPendingPayments();
    }

    @PostMapping("/confirm/{paymentId}")
    public Payment confirm(
            @PathVariable String paymentId,
            @RequestParam String adminId
    ) {
        return service.confirmPayment(paymentId, adminId);
    }

    @PostMapping("/reject/{paymentId}")
    public Payment reject(
            @PathVariable String paymentId,
            @RequestParam String adminId,
            @RequestParam String reason
    ) {
        return service.rejectPayment(paymentId, adminId, reason);
    }
}
