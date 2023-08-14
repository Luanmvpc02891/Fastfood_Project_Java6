package com.poly.entity;

import java.io.Serializable;

import jakarta.persistence.*;

import lombok.Data;

@Data
@Entity
@Table(name = "AccountRoles")
public class AccountRole implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accountroles_id")
    private int accountRolesId;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Roles role;

    // Getters and Setters
}