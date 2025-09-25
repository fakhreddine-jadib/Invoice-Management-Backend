package com.emsi.invoicemanagementapp.service;

import com.emsi.invoicemanagementapp.dto.InvoiceItemDTO;
import com.emsi.invoicemanagementapp.dto.InvoiceRequestDTO;
import com.emsi.invoicemanagementapp.dto.PaymentRequestDTO;
import com.emsi.invoicemanagementapp.enums.ClientTaxStatus;
import com.emsi.invoicemanagementapp.enums.InvoiceStatus;
import com.emsi.invoicemanagementapp.model.Client;
import com.emsi.invoicemanagementapp.model.Invoice;
import com.emsi.invoicemanagementapp.model.InvoiceItem;
import com.emsi.invoicemanagementapp.model.Payment;
import com.emsi.invoicemanagementapp.repository.ClientRepository;
import com.emsi.invoicemanagementapp.repository.InvoiceRepository;
import com.emsi.invoicemanagementapp.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class InvoiceService {

    // The hardcoded VAT rate is now removed.

    @Autowired private InvoiceRepository invoiceRepository;
    @Autowired private PaymentRepository paymentRepository;
    @Autowired private ClientRepository clientRepository;
    @Autowired private SettingService settingService; // <-- Inject SettingService

    public List<Invoice> getAllInvoices() { return invoiceRepository.findAll(); }
    public Optional<Invoice> getInvoiceById(Long id) { return invoiceRepository.findById(id); }
    public void deleteInvoice(Long id) { invoiceRepository.deleteById(id); }

    @Transactional
    public Invoice createInvoice(InvoiceRequestDTO invoiceRequest) {
        Client client = clientRepository.findById(invoiceRequest.getClient().getId())
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + invoiceRequest.getClient().getId()));

        Invoice invoice = new Invoice();
        invoice.setClient(client);
        invoice.setDueDate(invoiceRequest.getDueDate());
        invoice.setInvoiceReferences(invoiceRequest.getInvoiceReferences());
        invoice.setTermsAndConditions(invoiceRequest.getTermsAndConditions());
        invoice.setLegalMentions(invoiceRequest.getLegalMentions());

        List<InvoiceItem> items = mapDtosToItems(invoiceRequest.getInvoiceItems(), invoice);
        invoice.setInvoiceItems(items);

        recalculateInvoiceTotals(invoice);

        invoice.setIssueDate(LocalDate.now());
        invoice.setStatus(InvoiceStatus.UNPAID);
        invoice.setAmountPaid(BigDecimal.ZERO);
        invoice.setInvoiceNumber("INV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        return invoiceRepository.save(invoice);
    }

    @Transactional
    public Invoice updateInvoice(Long id, InvoiceRequestDTO invoiceRequest) {
        Invoice existingInvoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + id));
        Client client = clientRepository.findById(invoiceRequest.getClient().getId())
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + invoiceRequest.getClient().getId()));

        existingInvoice.setClient(client);
        existingInvoice.setDueDate(invoiceRequest.getDueDate());
        existingInvoice.setInvoiceReferences(invoiceRequest.getInvoiceReferences());
        existingInvoice.setTermsAndConditions(invoiceRequest.getTermsAndConditions());
        existingInvoice.setLegalMentions(invoiceRequest.getLegalMentions());

        existingInvoice.getInvoiceItems().clear();
        List<InvoiceItem> newItems = mapDtosToItems(invoiceRequest.getInvoiceItems(), existingInvoice);
        existingInvoice.getInvoiceItems().addAll(newItems);

        recalculateInvoiceTotals(existingInvoice);
        updateInvoiceStatus(existingInvoice);

        return invoiceRepository.save(existingInvoice);
    }

    @Transactional
    public Invoice addPayment(Long invoiceId, PaymentRequestDTO paymentRequest) {
        Invoice invoice = getInvoiceById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + invoiceId));

        Payment payment = new Payment();
        payment.setPaymentDate(paymentRequest.getPaymentDate());
        payment.setAmount(paymentRequest.getAmount());
        payment.setPaymentMethod(paymentRequest.getPaymentMethod());
        payment.setReference(paymentRequest.getReference());
        payment.setInvoice(invoice);

        paymentRepository.save(payment);

        invoice.setAmountPaid(invoice.getAmountPaid().add(payment.getAmount()));
        updateInvoiceStatus(invoice);

        return invoiceRepository.save(invoice);
    }

    private void recalculateInvoiceTotals(Invoice invoice) {
        BigDecimal subtotal = invoice.getInvoiceItems().stream()
                .map(item -> {
                    BigDecimal itemTotal = item.getUnitPrice().multiply(item.getQuantity());
                    item.setTotalPrice(itemTotal);
                    return itemTotal;
                })
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
    }

    private void updateInvoiceStatus(Invoice invoice) {
        BigDecimal total = invoice.getGrandTotal();
        BigDecimal paid = invoice.getAmountPaid();
        if (paid.compareTo(total) >= 0 && total.compareTo(BigDecimal.ZERO) > 0) {
            invoice.setStatus(InvoiceStatus.PAID);
        } else if (paid.compareTo(BigDecimal.ZERO) > 0) {
            invoice.setStatus(InvoiceStatus.PARTIALLY_PAID);
        } else {
            invoice.setStatus(InvoiceStatus.UNPAID);
        }
    }

    private List<InvoiceItem> mapDtosToItems(List<InvoiceItemDTO> dtos, Invoice invoice) {
        return dtos.stream().map(dto -> {
            InvoiceItem item = new InvoiceItem();
            item.setDescription(dto.getDescription());
            item.setQuantity(dto.getQuantity());
            item.setUnitPrice(dto.getUnitPrice());
            item.setInvoice(invoice);
            return item;
        }).collect(Collectors.toList());
    }
}