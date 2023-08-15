package com.poly.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.poly.entity.Account;


public interface AccountRepository extends JpaRepository<Account, Integer> {
	Account findByUsername(String username);
	Account findById(int account_id);

	
}

