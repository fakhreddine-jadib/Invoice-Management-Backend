package com.emsi.invoicemanagementapp.repository;

import com.emsi.invoicemanagementapp.model.QuoteItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuoteItemRepository extends JpaRepository<QuoteItem, Long> {
}