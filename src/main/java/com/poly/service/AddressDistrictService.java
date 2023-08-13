package com.poly.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.poly.entity.AddressDistrict;

@Service
public interface AddressDistrictService {

	List<AddressDistrict> findByAccountId(Integer accountId);

	List<AddressDistrict> findAll();

}
