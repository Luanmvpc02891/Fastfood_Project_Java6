package com.poly.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "Invoices")
public class Invoice implements Serializable {
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

	@Column(name = "total_amount")
	private double totalAmount;

	@Column(name = "payment_method")
	private int paymentMethod;

	@Column(name = "ship_fee")
	private double shipfee;

	@Column(name = "status")
	private boolean status;

	// Getters and Setters
}
