package com.poly.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poly.entity.Account;
import com.poly.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
	Role findByRoleName(int roleId);
}
