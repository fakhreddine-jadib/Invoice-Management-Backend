package com.emsi.invoicemanagementapp.repository;

import com.emsi.invoicemanagementapp.model.Invoice;
import com.emsi.invoicemanagementapp.model.InvoiceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the InvoiceItem entity.
 */
@Repository
public interface InvoiceItemRepository extends JpaRepository<InvoiceItem, Long> {

    // Find all line items for a specific invoice.
    List<InvoiceItem> findByInvoice(Invoice invoice);
}