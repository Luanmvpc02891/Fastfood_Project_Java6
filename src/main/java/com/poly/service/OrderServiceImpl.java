package com.poly.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.dao.OrderItemDao;
import com.poly.dao.OrdersDao;
import com.poly.entity.Order;
import com.poly.entity.OrderItem;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderItemDao orderItemsRepository;
    @Autowired
    OrdersDao orderDao;

}
