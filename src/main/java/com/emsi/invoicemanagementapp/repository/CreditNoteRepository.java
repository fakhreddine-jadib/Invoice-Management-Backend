package com.emsi.invoicemanagementapp.repository;

import com.emsi.invoicemanagementapp.model.CreditNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditNoteRepository extends JpaRepository<CreditNote, Long> {
}