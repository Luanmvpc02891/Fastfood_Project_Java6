package com.poly.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.dao.AddressCityDAO;
import com.poly.entity.AddressCity;

@Service
public class AddressCityServiceImp implements AddressCityService {
	@Autowired
	AddressCityDAO addressCityDAO;

	@Override
	public List<AddressCity> findByAccountId(Integer accountId) {
		List<AddressCity> listCitys = addressCityDAO.findAll();
		return listCitys;
	}

	@Override
	public List<AddressCity> findAll() {
		List<AddressCity> listCitys = addressCityDAO.findAll();
		return listCitys;
	}
}
