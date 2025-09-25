package com.emsi.invoicemanagementapp.controller;

import com.emsi.invoicemanagementapp.dto.InvoiceRequestDTO;
import com.emsi.invoicemanagementapp.dto.PaymentRequestDTO;
import com.emsi.invoicemanagementapp.model.Invoice;
import com.emsi.invoicemanagementapp.service.InvoiceService;
import com.emsi.invoicemanagementapp.service.PdfService; // <-- Import PdfService
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.List;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private PdfService pdfService; // <-- Inject PdfService

    // ... (all your other endpoints like getAllInvoices, createInvoice, etc. remain here) ...

    @GetMapping
    public List<Invoice> getAllInvoices() {
        return invoiceService.getAllInvoices();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Invoice> getInvoiceById(@PathVariable Long id) {
        return invoiceService.getInvoiceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Invoice createInvoice(@RequestBody InvoiceRequestDTO invoiceRequest) {
        return invoiceService.createInvoice(invoiceRequest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Invoice> updateInvoice(@PathVariable Long id, @RequestBody InvoiceRequestDTO invoiceRequest) {
        try {
            Invoice updatedInvoice = invoiceService.updateInvoice(id, invoiceRequest);
            return ResponseEntity.ok(updatedInvoice);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{invoiceId}/payments")
    public ResponseEntity<Invoice> addPaymentToInvoice(@PathVariable Long invoiceId, @RequestBody PaymentRequestDTO paymentRequest) {
        try {
            Invoice updatedInvoice = invoiceService.addPayment(invoiceId, paymentRequest);
            return ResponseEntity.ok(updatedInvoice);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable Long id) {
        return invoiceService.getInvoiceById(id)
                .map(invoice -> {
                    invoiceService.deleteInvoice(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // --- NEW: PDF Download Endpoint ---
    @GetMapping(value = "/{id}/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> downloadInvoicePdf(@PathVariable Long id) {
        Invoice invoice = invoiceService.getInvoiceById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + id));

        ByteArrayInputStream bis = pdfService.generateInvoicePdf(invoice);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=" + invoice.getInvoiceNumber() + ".pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
}