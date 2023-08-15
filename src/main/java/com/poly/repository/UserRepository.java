package com.poly.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poly.entity.Account;


public interface UserRepository extends JpaRepository<Account, Integer> {
	List<Account> findAll();
	Account findByUsername(String username);
	Account findByEmail(String email);
	
}
