package com.ragapi.controller;

import com.ragapi.service.RevenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/admin/dashboard", "/api/admin/dashboard"})
@RequiredArgsConstructor
public class AdminStatsController {

    private final RevenueService revenueService;

    @GetMapping("/revenue")
    public double revenue() {
        return revenueService.getTotalRevenue();
    }

    @GetMapping("/contracts/completed")
    public long completedContracts() {
        return revenueService.getTotalCompletedContracts();
    }
}
