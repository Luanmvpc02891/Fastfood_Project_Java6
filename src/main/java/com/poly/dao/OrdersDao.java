package com.poly.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.poly.entity.Account;
import com.poly.entity.Order;

public interface OrdersDao extends JpaRepository<Order, Integer> {

    Order findFirstByAccountAndTotalPrice(Account account, double totalPrice);

    Order findByAccount(Account accountId);

    Order findByAccountAccountId(Integer accountId);

}
