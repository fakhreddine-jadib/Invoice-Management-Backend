package com.emsi.invoicemanagementapp.service;

import com.emsi.invoicemanagementapp.model.Invoice;
import com.emsi.invoicemanagementapp.model.InvoiceItem;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

@Service
public class PdfService {

    public ByteArrayInputStream generateInvoicePdf(Invoice invoice) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, PageSize.A4);
            document.setMargins(30, 30, 30, 30);

            PdfFont boldFont = PdfFontFactory.createFont("Helvetica-Bold");
            PdfFont regularFont = PdfFontFactory.createFont("Helvetica");
            Color headerColor = new DeviceRgb(23, 162, 184);

            // --- Header Section ---
            Table headerTable = new Table(UnitValue.createPercentArray(new float[]{1, 1}));
            headerTable.setWidth(UnitValue.createPercentValue(100));
            Paragraph companyInfo = new Paragraph("Your Company Name\nYour Address\nYour City, Postal Code\nYour Email")
                    .setFont(regularFont).setFontSize(10);
            headerTable.addCell(new Cell().add(companyInfo).setBorder(Border.NO_BORDER));
            Paragraph invoiceTitle = new Paragraph("INVOICE")
                    .setFont(boldFont).setFontSize(28).setFontColor(headerColor)
                    .setTextAlignment(TextAlignment.RIGHT);
            headerTable.addCell(new Cell().add(invoiceTitle).setBorder(Border.NO_BORDER).setVerticalAlignment(VerticalAlignment.MIDDLE));
            document.add(headerTable);
            document.add(new Paragraph("\n"));

            // --- Invoice and Client Info Section ---
            Table infoTable = new Table(UnitValue.createPercentArray(new float[]{1, 1, 1}));
            infoTable.setWidth(UnitValue.createPercentValue(100)).setMarginTop(20);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM, yyyy");
            infoTable.addCell(createCell("Bill To:", boldFont, false));
            infoTable.addCell(createCell("Invoice #:", boldFont, false));
            infoTable.addCell(createCell(invoice.getInvoiceNumber(), regularFont, true));
            infoTable.addCell(createCell(invoice.getClient().getCompanyName(), regularFont, false));
            infoTable.addCell(createCell("Issue Date:", boldFont, false));
            infoTable.addCell(createCell(invoice.getIssueDate().format(formatter), regularFont, true));
            infoTable.addCell(createCell(invoice.getClient().getAddress() != null ? invoice.getClient().getAddress() : "", regularFont, false));
            infoTable.addCell(createCell("Due Date:", boldFont, false));
            infoTable.addCell(createCell(invoice.getDueDate().format(formatter), regularFont, true));
            document.add(infoTable);

            // --- Line Items Table ---
            Table itemsTable = new Table(UnitValue.createPercentArray(new float[]{5, 1, 2, 2}));
            itemsTable.setWidth(UnitValue.createPercentValue(100)).setMarginTop(20);
            itemsTable.addHeaderCell(createHeaderCell("Description"));
            itemsTable.addHeaderCell(createHeaderCell("Qty", TextAlignment.RIGHT));
            itemsTable.addHeaderCell(createHeaderCell("Unit Price", TextAlignment.RIGHT));
            itemsTable.addHeaderCell(createHeaderCell("Total", TextAlignment.RIGHT));
            for (InvoiceItem item : invoice.getInvoiceItems()) {
                itemsTable.addCell(new Cell().add(new Paragraph(item.getDescription()).setFont(regularFont)).setBorder(new SolidBorder(ColorConstants.LIGHT_GRAY, 0.5f)));
                itemsTable.addCell(new Cell().add(new Paragraph(String.valueOf(item.getQuantity()))).setTextAlignment(TextAlignment.RIGHT).setFont(regularFont).setBorder(new SolidBorder(ColorConstants.LIGHT_GRAY, 0.5f)));
                itemsTable.addCell(new Cell().add(new Paragraph(item.getUnitPrice().toPlainString() + " MAD")).setTextAlignment(TextAlignment.RIGHT).setFont(regularFont).setBorder(new SolidBorder(ColorConstants.LIGHT_GRAY, 0.5f)));
                itemsTable.addCell(new Cell().add(new Paragraph(item.getTotalPrice().toPlainString() + " MAD")).setTextAlignment(TextAlignment.RIGHT).setFont(regularFont).setBorder(new SolidBorder(ColorConstants.LIGHT_GRAY, 0.5f)));
            }
            document.add(itemsTable);

            // --- Totals Section ---
            Table totalsTable = new Table(UnitValue.createPercentArray(new float[]{2, 1}));
            totalsTable.setWidth(UnitValue.createPercentValue(40)).setMarginTop(20);
            totalsTable.setHorizontalAlignment(com.itextpdf.layout.properties.HorizontalAlignment.RIGHT);

            totalsTable.addCell(createTotalsCell("Subtotal:", TextAlignment.RIGHT));
            totalsTable.addCell(createTotalsCell(invoice.getSubtotal().toPlainString() + " MAD", TextAlignment.RIGHT));

            totalsTable.addCell(createTotalsCell("VAT (" + invoice.getVatRate().multiply(new BigDecimal("100")).toPlainString() + "%):", TextAlignment.RIGHT));
            totalsTable.addCell(createTotalsCell(invoice.getVatAmount().toPlainString() + " MAD", TextAlignment.RIGHT));

            totalsTable.addCell(createTotalsCell("Amount Paid:", TextAlignment.RIGHT));
            totalsTable.addCell(createTotalsCell(invoice.getAmountPaid().toPlainString() + " MAD", TextAlignment.RIGHT));

            Cell grandTotalLabelCell = createTotalsCell("Grand Total:", TextAlignment.RIGHT);
            grandTotalLabelCell.setBackgroundColor(headerColor).setFontColor(ColorConstants.WHITE).setFont(boldFont);
            totalsTable.addCell(grandTotalLabelCell);

            Cell grandTotalValueCell = createTotalsCell(invoice.getGrandTotal().toPlainString() + " MAD", TextAlignment.RIGHT);
            grandTotalValueCell.setBackgroundColor(headerColor).setFontColor(ColorConstants.WHITE).setFont(boldFont);
            totalsTable.addCell(grandTotalValueCell);

            document.add(totalsTable);

            // --- Footer ---
            Paragraph footer = new Paragraph("Thank you for your business!")
                    .setTextAlignment(TextAlignment.CENTER).setFont(regularFont).setFontSize(10).setMarginTop(40);
            document.add(footer);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    private Cell createCell(String text, PdfFont font, boolean isRightAligned) {
        Cell cell = new Cell().add(new Paragraph(text).setFont(font).setFontSize(10));
        cell.setBorder(Border.NO_BORDER);
        if (isRightAligned) {
            cell.setTextAlignment(TextAlignment.RIGHT);
        }
        return cell;
    }

    private Cell createHeaderCell(String text) {
        return createHeaderCell(text, TextAlignment.LEFT);
    }

    private Cell createHeaderCell(String text, TextAlignment alignment) {
        try {
            PdfFont boldFont = PdfFontFactory.createFont("Helvetica-Bold");
            return new Cell().add(new Paragraph(text).setFont(boldFont).setFontSize(10))
                    .setTextAlignment(alignment)
                    .setBackgroundColor(new DeviceRgb(220, 220, 220))
                    .setBorder(new SolidBorder(ColorConstants.LIGHT_GRAY, 0.5f));
        } catch (Exception e) { return new Cell(); }
    }

    private Cell createTotalsCell(String text, TextAlignment alignment) {
        try {
            PdfFont font = PdfFontFactory.createFont("Helvetica");
            return new Cell().add(new Paragraph(text).setFont(font).setFontSize(10))
                    .setTextAlignment(alignment)
                    .setBorder(Border.NO_BORDER)
                    .setPadding(5);
        } catch (Exception e) { return new Cell(); }
    }
}