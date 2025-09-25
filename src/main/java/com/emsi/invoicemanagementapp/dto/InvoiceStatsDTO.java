package com.emsi.invoicemanagementapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InvoiceStatsDTO {
    private long paidCount;
    private long unpaidCount;
    private long partiallyPaidCount;
    private long creditedCount;
}