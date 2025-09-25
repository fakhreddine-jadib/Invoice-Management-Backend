package com.emsi.invoicemanagementapp.enums;

/**
 * Defines the possible statuses of a quote.
 */
public enum QuoteStatus {
    DRAFT,      // Quote is being prepared
    SENT,       // Quote has been sent to the client
    ACCEPTED,   // Client has approved the quote
    REJECTED,   // Client has declined the quote
    INVOICED    // Quote has been converted to an invoice
}