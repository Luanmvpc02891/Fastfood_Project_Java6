package com.poly.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.*;

import lombok.Data;

@Data
@Entity
@Table(name = "Invoices")
public class Invoices implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_id")
    private int invoiceId;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(name = "invoice_date")
    private Date invoiceDate;

    @Column(name = "ship_fee")
    private double shipFee;

    @Column(name = "total_amount")
    private double totalAmount;

    @ManyToOne
    @JoinColumn(name = "payment_method")
    private PaymentMethod paymentMethod;

    // Getters and Setters
}