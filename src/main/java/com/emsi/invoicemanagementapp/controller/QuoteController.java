package com.emsi.invoicemanagementapp.controller;

import com.emsi.invoicemanagementapp.dto.QuoteRequestDTO;
import com.emsi.invoicemanagementapp.model.Invoice;
import com.emsi.invoicemanagementapp.model.Quote;
import com.emsi.invoicemanagementapp.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quotes")
public class QuoteController {

    @Autowired
    private QuoteService quoteService;

    @GetMapping
    public List<Quote> getAllQuotes() {
        return quoteService.getAllQuotes();
    }

    @PostMapping
    public Quote createQuote(@RequestBody QuoteRequestDTO quoteRequest) {
        return quoteService.createQuote(quoteRequest);
    }

    @PostMapping("/{id}/convert-to-invoice")
    public ResponseEntity<Invoice> convertToInvoice(@PathVariable Long id) {
        try {
            Invoice invoice = quoteService.convertToInvoice(id);
            return ResponseEntity.ok(invoice);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}