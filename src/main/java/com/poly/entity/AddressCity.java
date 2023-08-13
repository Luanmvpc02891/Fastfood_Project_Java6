package com.poly.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "address_city")
public class AddressCity implements Serializable {
    @Id
    @Column(name = "addresscity_id")
    private int addressCityId;

    @Column(name = "name")
    private String name;

    // Getters and Setters
}
