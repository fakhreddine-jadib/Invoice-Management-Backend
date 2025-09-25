package com.emsi.invoicemanagementapp.dto;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;
@Data
public class InvoiceRequestDTO {
    private LocalDate dueDate;
    private ClientIdDTO client;
    private List<InvoiceItemDTO> invoiceItems;
    // CORRECTED: Renamed field to match entity
    private String invoiceReferences;
    private String termsAndConditions;
    private String legalMentions;
    @Data
    public static class ClientIdDTO {
        private Long id;
    }
}