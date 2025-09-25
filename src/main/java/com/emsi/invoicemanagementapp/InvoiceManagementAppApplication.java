package com.emsi.invoicemanagementapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InvoiceManagementAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(InvoiceManagementAppApplication.class, args);
        System.out.println("Invoice Management App Started");
    }
}