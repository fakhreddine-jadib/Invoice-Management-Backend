package com.emsi.invoicemanagementapp.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PaymentRequestDTO {
    private LocalDate paymentDate;
    private BigDecimal amount;
    private String paymentMethod;
    private String reference;
}