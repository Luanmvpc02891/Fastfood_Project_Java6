package com.poly.restAdmin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.dao.DiscountDAO;
import com.poly.entity.DiscountCode;

@CrossOrigin("*")
@RestController
@RequestMapping("/Discount")
public class DiscountRestController {

    @Autowired
    DiscountDAO dao;

    @GetMapping("/load")
    public List<DiscountCode> getAllDiscount() {
        return dao.findAll();
    }

}
