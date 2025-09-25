package com.emsi.invoicemanagementapp.dto;
import lombok.Data;
import java.math.BigDecimal;
@Data
public class QuoteItemDTO {
    private String description;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
}