package com.poly.entity;

import java.io.Serializable;

import jakarta.persistence.*;

import lombok.Data;

@Data
@Entity
@Table(name = "PaymentMethods")
public class PaymentMethod implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_method_id")
    private int paymentMethodId;

    @Column(name = "method_name")
    private String methodName;

    @Column(name = "sender_id")
    private int senderId;

    @Column(name = "sender_bank_account")
    private String senderBankAccount;

    // Getters and Setters
}