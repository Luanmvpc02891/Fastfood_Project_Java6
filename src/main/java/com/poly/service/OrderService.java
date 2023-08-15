package com.poly.service;

import org.springframework.stereotype.Service;

import com.poly.entity.Order;

@Service
public interface OrderService {

    Order findById(Integer orderId);

}
