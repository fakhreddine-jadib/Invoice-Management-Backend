package com.emsi.invoicemanagementapp.repository;

import com.emsi.invoicemanagementapp.model.Client;
import com.emsi.invoicemanagementapp.model.Quote;
import com.emsi.invoicemanagementapp.enums.QuoteStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the Quote entity.
 */
@Repository
public interface QuoteRepository extends JpaRepository<Quote, Long> {

    // Find all quotes for a specific client
    List<Quote> findByClient(Client client);

    // Find all quotes with a specific status
    List<Quote> findByStatus(QuoteStatus status);
}