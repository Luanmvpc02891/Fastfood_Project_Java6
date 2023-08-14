package com.poly.entity;

import java.io.Serializable;
import jakarta.persistence.*;

import lombok.Data;

@Data
@Entity
@Table(name = "InvoiceItems")
public class InvoiceItems implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoiceitems_id")
    private int invoiceItemsId;

    @ManyToOne
    @JoinColumn(name = "invoice_id")
    private Invoices invoice;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "unit_price")
    private double unitPrice;

    // Getters and Setters
}