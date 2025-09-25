package com.emsi.invoicemanagementapp.repository;

import com.emsi.invoicemanagementapp.model.Invoice;
import com.emsi.invoicemanagementapp.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the Payment entity.
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // Find all payments associated with a specific invoice.
    // This will be useful for calculating the total amount paid for an invoice.
    List<Payment> findByInvoice(Invoice invoice);
}