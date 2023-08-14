package com.poly.entity;

import java.io.Serializable;

import jakarta.persistence.*;

import lombok.Data;

@Data
@Entity
@Table(name = "Items")
public class Item implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private int itemId;

    @Column(name = "name")
    private String name;

    @Column(name = "image")
    private String image;

    @Column(name = "price")
    private double price;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "status")
    private boolean status;

    @ManyToOne
    @JoinColumn(name = "code_id")
    private DiscountCode codeId;

    @Column(name = "active")
    private boolean active;

    // Getters and Setters
}