package com.poly.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.poly.entity.AddressCity;

@Service
public interface AddressCityService {

	List<AddressCity> findByAccountId(Integer accountId);
	
	List<AddressCity> findAll();
}
