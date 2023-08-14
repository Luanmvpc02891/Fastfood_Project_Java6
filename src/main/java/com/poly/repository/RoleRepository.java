package com.poly.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poly.entity.Account;
import com.poly.entity.Roles;

public interface RoleRepository extends JpaRepository<Roles, Integer> {
	Roles findByRoleName(int roleId);
}
