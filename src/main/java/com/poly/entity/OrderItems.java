package com.poly.entity;

import java.io.Serializable;
import jakarta.persistence.*;

import lombok.Data;

@Data
@Entity
@Table(name = "order_items")
public class OrderItems implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderitems_id")
    private int orderItemsId;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @Column(name = "quantity")
    private int quantity;

}