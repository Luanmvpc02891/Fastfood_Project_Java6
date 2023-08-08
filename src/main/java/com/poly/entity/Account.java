package com.poly.entity;

import java.io.Serializable;

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
@Table(name = "Accounts")
public class Account implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "account_id")
	private int accountId;

	@Column(name = "username")
	private String username;

	@Column(name = "pass_work")
	private String password;

	@Column(name = "full_name")
	private String fullName;

	@Column(name = "email")
	private String email;

	@Column(name = "phone_number")
	private int phoneNumber;

	@Column(name = "image")
	private String image;

	@ManyToOne
	@JoinColumn(name = "address_city")
	private AddressCity addressCity;

	@ManyToOne
	@JoinColumn(name = "address_district")
	private AddressDistrict addressDistrict;

	@Column(name = "active")
	private boolean active;

	// Getters and Setters

}
