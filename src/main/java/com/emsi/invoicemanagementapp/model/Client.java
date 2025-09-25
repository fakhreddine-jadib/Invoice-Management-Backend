package com.emsi.invoicemanagementapp.model;

import com.emsi.invoicemanagementapp.enums.ClientTaxStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference; // <-- This has changed
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import java.math.BigDecimal;

@Entity
@Table(name = "clients")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false, unique = true)
    private String iceFiscalId;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String defaultPaymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClientTaxStatus taxStatus;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference // <-- CORRECTED: This is the parent side and should be included
    private List<Invoice> invoices;

    @Column(nullable = false) // Ensures it's never null in the database
    private BigDecimal balance = BigDecimal.ZERO; // Initializes the value to 0

}