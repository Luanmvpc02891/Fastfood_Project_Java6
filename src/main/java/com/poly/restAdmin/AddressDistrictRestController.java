package com.poly.restAdmin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.dao.AddressDistristDao;
import com.poly.entity.AddressDistrict;

@CrossOrigin("*")
@RestController
@RequestMapping("/AddressDistrict")
public class AddressDistrictRestController {
    @Autowired
    AddressDistristDao dao;

    @GetMapping("/load")
    public List<AddressDistrict> getAllAccounts() {
        return dao.findAll();
    }
}
