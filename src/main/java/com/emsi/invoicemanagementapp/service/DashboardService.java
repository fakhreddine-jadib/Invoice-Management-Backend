package com.emsi.invoicemanagementapp.service;

import com.emsi.invoicemanagementapp.dto.DashboardStatsDTO;
import com.emsi.invoicemanagementapp.dto.MonthlyRevenueDTO;
import com.emsi.invoicemanagementapp.enums.InvoiceStatus;
import com.emsi.invoicemanagementapp.repository.ClientRepository;
import com.emsi.invoicemanagementapp.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.emsi.invoicemanagementapp.dto.InvoiceStatsDTO;
import com.emsi.invoicemanagementapp.enums.InvoiceStatus;


import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    public DashboardStatsDTO getDashboardStats() {
        long totalClients = clientRepository.count();
        long totalInvoices = invoiceRepository.count();

        // CORRECTED: Use getGrandTotal() instead of getTotalAmount()
        BigDecimal totalRevenue = invoiceRepository.findByStatus(InvoiceStatus.PAID)
                .stream()
                .map(invoice -> invoice.getGrandTotal())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new DashboardStatsDTO(totalClients, totalInvoices, totalRevenue);
    }

    public List<MonthlyRevenueDTO> getMonthlyRevenue() {
        return invoiceRepository.findMonthlyRevenue().stream()
                .map(result -> new MonthlyRevenueDTO(
                        (String) result[0],
                        (BigDecimal) result[1]
                ))
                .collect(Collectors.toList());
    }

    public InvoiceStatsDTO getInvoiceStats() {
        long paid = invoiceRepository.countByStatus(InvoiceStatus.PAID);
        long unpaid = invoiceRepository.countByStatus(InvoiceStatus.UNPAID);
        long partiallyPaid = invoiceRepository.countByStatus(InvoiceStatus.PARTIALLY_PAID);
        long credited = invoiceRepository.countByStatus(InvoiceStatus.CREDITED);

        return new InvoiceStatsDTO(paid, unpaid, partiallyPaid, credited);
    }

}