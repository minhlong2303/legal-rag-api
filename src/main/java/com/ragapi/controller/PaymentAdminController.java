package com.ragapi.controller;

import com.ragapi.entity.Payment;
import com.ragapi.service.PaymentAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/admin/payment", "/api/admin/payment"})
@RequiredArgsConstructor
public class PaymentAdminController {

    private final PaymentAdminService paymentAdminService;

    @PostMapping("/confirm/{paymentId}")
    public Payment confirmPayment(
            @PathVariable String paymentId,
            @RequestParam String systemAccountId,
            @RequestParam String systemAccountName
    ) {
        return paymentAdminService.confirmPaymentBySystem(
                paymentId,
                systemAccountId,
                systemAccountName
        );
    }
}
