package com.poly.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poly.entity.Account;
import com.poly.entity.AccountRoles;
import com.poly.entity.Role;

public interface AccountRoleDAO extends JpaRepository<AccountRoles, Integer> {
    AccountRoles findById(int roleId);

    AccountRoles findByAccountAndRole(Account account, Role role);
}
