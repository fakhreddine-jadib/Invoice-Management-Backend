package com.emsi.invoicemanagementapp.dto;

import com.emsi.invoicemanagementapp.enums.ClientTaxStatus;
import lombok.Data;

@Data
public class ClientRequestDTO {
    private String companyName;
    private String iceFiscalId;
    private String address;
    private String email;
    private String phone;
    private String defaultPaymentMethod;
    private ClientTaxStatus taxStatus; // <-- Add this line
}