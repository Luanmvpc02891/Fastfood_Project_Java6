package com.poly.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "address_district")
public class AddressDistrict implements Serializable {
    @Id
    @Column(name = "addressdistrict_id")
    private int addressDistrictId;

    @Column(name = "name")
    private String name;

    // Getters and Setters
}
