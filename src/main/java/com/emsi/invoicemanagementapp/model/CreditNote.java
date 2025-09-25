package com.emsi.invoicemanagementapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "credit_notes")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CreditNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String creditNoteNumber;

    @Column(nullable = false)
    private LocalDate issueDate;

    @Column(nullable = false)
    private BigDecimal amount;

    private String reason;

    @OneToOne
    @JoinColumn(name = "original_invoice_id", nullable = false, unique = true)
    @JsonBackReference
    private Invoice originalInvoice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    @JsonBackReference("client-creditnote") // <-- ADD THIS ANNOTATION
    private Client client;
}