package com.poly.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poly.entity.Account;

public interface AccountDao extends JpaRepository<Account, Integer> {

	Account findByUsername(String username);

	Account findById(int accountId);

	List<Account> findAll();

	Account findByAccountId(int i);

}
