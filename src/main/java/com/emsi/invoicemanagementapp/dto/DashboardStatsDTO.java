package com.emsi.invoicemanagementapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class DashboardStatsDTO {
    private long totalClients;
    private long totalInvoices;
    private BigDecimal totalRevenue;
}