package com.emsi.invoicemanagementapp.repository;

import com.emsi.invoicemanagementapp.model.Client;
import com.emsi.invoicemanagementapp.model.Invoice;
import com.emsi.invoicemanagementapp.enums.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.emsi.invoicemanagementapp.enums.InvoiceStatus;


import java.util.List;

/**
 * Spring Data JPA repository for the Invoice entity.
 */
@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    // Spring Data JPA will automatically create a query for this method name.
    // It will find all invoices that belong to a specific client.
    List<Invoice> findByClient(Client client);

    // This method will find all invoices that have a specific status.
    List<Invoice> findByStatus(InvoiceStatus status);

    // Custom query to calculate the revenue per month from the 'payments' table
    @Query(value = "SELECT DATE_FORMAT(payment_date, '%Y-%m') as month, SUM(amount) as revenue " +
            "FROM payments " +
            "GROUP BY DATE_FORMAT(payment_date, '%Y-%m') " +
            "ORDER BY month", nativeQuery = true)
    List<Object[]> findMonthlyRevenue();

    long countByStatus(InvoiceStatus status);

}