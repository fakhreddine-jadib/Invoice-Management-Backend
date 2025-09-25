package com.emsi.invoicemanagementapp.controller;

import com.emsi.invoicemanagementapp.model.CreditNote;
import com.emsi.invoicemanagementapp.service.CreditNoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CreditNoteController {

    @Autowired
    private CreditNoteService creditNoteService;

    @PostMapping("/invoices/{invoiceId}/credit-note")
    public ResponseEntity<CreditNote> createCreditNote(@PathVariable Long invoiceId, @RequestBody Map<String, String> payload) {
        try {
            String reason = payload.get("reason");
            CreditNote creditNote = creditNoteService.createCreditNoteForInvoice(invoiceId, reason);
            return ResponseEntity.ok(creditNote);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/credit-notes")
    public List<CreditNote> getAllCreditNotes() {
        return creditNoteService.getAllCreditNotes();
    }
}