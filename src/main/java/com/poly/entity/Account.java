package com.poly.entity;

import java.io.Serializable;
import java.util.List;



import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "Accounts")
public class Account implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private int accountId;

//	@NotBlank(message = "{NotBlank.user.username}")
    @Column(name = "username" , nullable=false)
    private String username;

//	@NotBlank(message = "{NotBlank.user.password}")
    @Column(name = "pass_work" , nullable = false)
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
    
    @JsonIgnore
	@OneToMany(mappedBy = "account", fetch = FetchType.EAGER)
	List<AccountRole> authorities;

    // Getters and Setters
}
