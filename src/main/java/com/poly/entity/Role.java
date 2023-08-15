package com.poly.entity;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.*;

import lombok.Data;


@Data
@Entity
@Table(name = "Roles")
public class Role implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private int roleId;

    @Column(name = "role_name")
    private String roleName;

 

    // Getters and Setters
}
