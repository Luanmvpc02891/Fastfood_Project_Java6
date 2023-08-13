package com.poly.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poly.entity.AddressCity;

public interface AddressDao extends JpaRepository<AddressCity, Integer> {

}
