package com.poly.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.dao.AddressDistrictDAO;
import com.poly.entity.AddressCity;
import com.poly.entity.AddressDistrict;

@Service
public class AddressDistrictServiceImp implements AddressDistrictService {
	@Autowired
	AddressDistrictDAO addressDistrictDAO;
	
	@Override
	public List<AddressDistrict> findByAccountId(Integer accountId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AddressDistrict> findAll() {
		List<AddressDistrict> listDistricts = addressDistrictDAO.findAll();
		return listDistricts;
	}

	

}
