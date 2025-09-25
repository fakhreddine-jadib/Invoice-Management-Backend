package com.emsi.invoicemanagementapp.model;

import com.emsi.invoicemanagementapp.enums.InvoiceStatus;
import com.fasterxml.jackson.annotation.JsonBackReference; // <-- This has changed
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "invoices")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String invoiceNumber;

    @Column(nullable = false)
    private LocalDate issueDate;

    @Column(nullable = false)
    private LocalDate dueDate;

    @Column(nullable = false)
    private BigDecimal subtotal;

    @Column(nullable = false)
    private BigDecimal vatRate;

    @Column(nullable = false)
    private BigDecimal vatAmount;

    @Column(nullable = false)
    private BigDecimal grandTotal;

    @Column(nullable = false)
    private BigDecimal amountPaid;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvoiceStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    @JsonBackReference // <-- CORRECTED: This is the child side and will be excluded to prevent loops
    private Client client;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<InvoiceItem> invoiceItems;

    @Column(length = 512)
    private String invoiceReferences;

    @Column(columnDefinition = "TEXT")
    private String termsAndConditions;

    @Column(columnDefinition = "TEXT")
    private String legalMentions;

    @OneToOne(mappedBy = "originalInvoice", cascade = CascadeType.ALL)
    @JsonManagedReference
    private CreditNote creditNote;

}