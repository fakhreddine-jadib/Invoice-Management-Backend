package com.emsi.invoicemanagementapp.service;

import com.emsi.invoicemanagementapp.enums.InvoiceStatus;
import com.emsi.invoicemanagementapp.model.CreditNote;
import com.emsi.invoicemanagementapp.model.Invoice;
import com.emsi.invoicemanagementapp.repository.CreditNoteRepository;
import com.emsi.invoicemanagementapp.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class CreditNoteService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private CreditNoteRepository creditNoteRepository;

    @Transactional
    public CreditNote createCreditNoteForInvoice(Long invoiceId, String reason) {
        Invoice originalInvoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + invoiceId));

        if (originalInvoice.getStatus() == InvoiceStatus.CANCELLED || originalInvoice.getStatus() == InvoiceStatus.CREDITED) {
            throw new IllegalStateException("Invoice is already cancelled or credited.");
        }

        CreditNote creditNote = new CreditNote();
        creditNote.setCreditNoteNumber("CN-" + originalInvoice.getInvoiceNumber());
        creditNote.setIssueDate(LocalDate.now());
        creditNote.setAmount(originalInvoice.getGrandTotal());
        creditNote.setReason(reason);
        creditNote.setClient(originalInvoice.getClient());
        creditNote.setOriginalInvoice(originalInvoice);

        originalInvoice.setStatus(InvoiceStatus.CREDITED);
        invoiceRepository.save(originalInvoice);

        return creditNoteRepository.save(creditNote);
    }

    public List<CreditNote> getAllCreditNotes() {
        return creditNoteRepository.findAll();
    }
}