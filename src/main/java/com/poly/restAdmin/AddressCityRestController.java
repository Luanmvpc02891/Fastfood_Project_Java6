package com.poly.restAdmin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails.Address;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.dao.AddressCityDAO;
import com.poly.entity.Account;
import com.poly.entity.AddressCity;

@CrossOrigin("*")
@RestController
@RequestMapping("/Addresscity")
public class AddressCityRestController {

    @Autowired
    AddressCityDAO dao;

    @GetMapping("/load")
    public List<AddressCity> getAllAccounts() {
        return dao.findAll();
    }

}
