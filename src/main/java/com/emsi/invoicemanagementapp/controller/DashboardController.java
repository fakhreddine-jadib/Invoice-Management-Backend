package com.emsi.invoicemanagementapp.controller;

import com.emsi.invoicemanagementapp.dto.DashboardStatsDTO;
import com.emsi.invoicemanagementapp.dto.MonthlyRevenueDTO;
import com.emsi.invoicemanagementapp.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emsi.invoicemanagementapp.dto.InvoiceStatsDTO;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/stats")
    public DashboardStatsDTO getStats() {
        return dashboardService.getDashboardStats();
    }

    @GetMapping("/revenue-by-month")
    public List<MonthlyRevenueDTO> getMonthlyRevenue() {
        return dashboardService.getMonthlyRevenue();
    }

    @GetMapping("/invoice-stats")
    public InvoiceStatsDTO getInvoiceStats() {
        return dashboardService.getInvoiceStats();
    }
}