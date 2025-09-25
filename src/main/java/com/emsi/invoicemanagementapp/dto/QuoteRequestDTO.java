package com.emsi.invoicemanagementapp.dto;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;
@Data
public class QuoteRequestDTO {
    private LocalDate expiryDate;
    private ClientIdDTO client;
    private List<QuoteItemDTO> quoteItems;
    @Data
    public static class ClientIdDTO {
        private Long id;
    }
}