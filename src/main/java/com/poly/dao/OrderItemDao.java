package com.poly.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poly.entity.Account;
import com.poly.entity.Item;
import com.poly.entity.Order;
import com.poly.entity.OrderItem;

public interface OrderItemDao extends JpaRepository<OrderItem, Integer> {

    List<OrderItem> findByOrder_Account_AccountId(Integer accountId);

    OrderItem findByOrderAndItem(Order order, Item item);

    int countByOrderAccountAndItem(Account account, Item item);
}
