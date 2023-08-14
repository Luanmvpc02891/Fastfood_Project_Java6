package com.poly.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.poly.dao.ItemDao;
import com.poly.dao.OrderItemDao;
import com.poly.dao.OrdersDao;
import com.poly.entity.Category;
import com.poly.entity.Item;
import com.poly.entity.Order;
import com.poly.entity.OrderItem;
import com.poly.service.OrderItemService;
import com.poly.service.SessionService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CartController {
	@Autowired
	ItemDao dao;
	@Autowired
	OrderItemDao dao1;
	@Autowired
	OrdersDao dao2;
	@Autowired
	private OrderItemService cartService;

	@Autowired
	SessionService session;

	@GetMapping("/cart/{itemId}")
	public String addToCart(@PathVariable int itemId, Model model) {
		return "cart";
	}
}
