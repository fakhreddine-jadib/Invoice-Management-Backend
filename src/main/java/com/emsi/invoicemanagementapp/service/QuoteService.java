package com.emsi.invoicemanagementapp.service;

import com.emsi.invoicemanagementapp.dto.QuoteRequestDTO;
import com.emsi.invoicemanagementapp.enums.ClientTaxStatus;
import com.emsi.invoicemanagementapp.enums.InvoiceStatus;
import com.emsi.invoicemanagementapp.enums.QuoteStatus;
import com.emsi.invoicemanagementapp.model.*;
import com.emsi.invoicemanagementapp.repository.ClientRepository;
import com.emsi.invoicemanagementapp.repository.InvoiceRepository;
import com.emsi.invoicemanagementapp.repository.QuoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class QuoteService {

    @Autowired private QuoteRepository quoteRepository;
    @Autowired private ClientRepository clientRepository;
    @Autowired private InvoiceRepository invoiceRepository;
    @Autowired private SettingService settingService; // <-- Inject SettingService

    public List<Quote> getAllQuotes() {
        return quoteRepository.findAll();
    }

    @Transactional
    public Quote createQuote(QuoteRequestDTO quoteRequest) {
        Client client = clientRepository.findById(quoteRequest.getClient().getId())
                .orElseThrow(() -> new RuntimeException("Client not found"));

        Quote quote = new Quote();
        quote.setClient(client);
        quote.setExpiryDate(quoteRequest.getExpiryDate());
        quote.setIssueDate(LocalDate.now());
        quote.setStatus(QuoteStatus.DRAFT);
        quote.setQuoteNumber("QUO-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        List<QuoteItem> items = quoteRequest.getQuoteItems().stream().map(dto -> {
            QuoteItem item = new QuoteItem();
            item.setDescription(dto.getDescription());
            item.setQuantity(dto.getQuantity());
            item.setUnitPrice(dto.getUnitPrice());
            item.setTotalPrice(dto.getUnitPrice().multiply(dto.getQuantity()));
            item.setQuote(quote);
            return item;
        }).collect(Collectors.toList());
        quote.setQuoteItems(items);

        BigDecimal total = items.stream().map(QuoteItem::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        quote.setTotalAmount(total);

        return quoteRepository.save(quote);
    }

    @Transactional
    public Invoice convertToInvoice(Long quoteId) {
        Quote quote = quoteRepository.findById(quoteId)
                .orElseThrow(() -> new RuntimeException("Quote not found"));

        if (quote.getStatus() == QuoteStatus.INVOICED) {
            throw new IllegalStateException("Quote has already been converted to an invoice.");
        }

        Invoice invoice = new Invoice();
        invoice.setClient(quote.getClient());
        invoice.setIssueDate(LocalDate.now());
        invoice.setDueDate(LocalDate.now().plusDays(30));
        invoice.setStatus(InvoiceStatus.UNPAID);
        invoice.setAmountPaid(BigDecimal.ZERO);
        invoice.setInvoiceNumber("INV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        List<InvoiceItem> invoiceItems = quote.getQuoteItems().stream().map(quoteItem -> {
            InvoiceItem invoiceItem = new InvoiceItem();
            invoiceItem.setDescription(quoteItem.getDescription());
            invoiceItem.setQuantity(quoteItem.getQuantity());
            invoiceItem.setUnitPrice(quoteItem.getUnitPrice());
            invoiceItem.setTotalPrice(quoteItem.getTotalPrice());
            invoiceItem.setInvoice(invoice);
            return invoiceItem;
        }).collect(Collectors.toList());
        invoice.setInvoiceItems(invoiceItems);

        BigDecimal subtotal = invoice.getInvoiceItems().stream()
                .map(InvoiceItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        invoice.setSubtotal(subtotal);

        if (invoice.getClient().getTaxStatus() == ClientTaxStatus.VAT_APPLICABLE) {
            BigDecimal currentVatRate = settingService.getVatRate(); // <-- Use the service
            invoice.setVatRate(currentVatRate);
            BigDecimal vatAmount = subtotal.multiply(currentVatRate).setScale(2, RoundingMode.HALF_UP);
            invoice.setVatAmount(vatAmount);
        } else {
            invoice.setVatRate(BigDecimal.ZERO);
            invoice.setVatAmount(BigDecimal.ZERO);
        }

        invoice.setGrandTotal(invoice.getSubtotal().add(invoice.getVatAmount()));

        quote.setStatus(QuoteStatus.INVOICED);
        quoteRepository.save(quote);

        return invoiceRepository.save(invoice);
    }
}