package com.emsi.invoicemanagementapp.enums;

/**
 * Defines the user roles for access control.
 */
public enum Role {
    ADMIN,      // Can do everything
    ACCOUNTANT, // Can manage invoices, clients, payments
    READER      // Can only view data
}