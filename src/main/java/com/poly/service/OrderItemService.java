package com.poly.service;

import java.util.Date;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.dao.AccountDao;
import com.poly.dao.OrderItemDao;
import com.poly.dao.OrdersDao;
import com.poly.entity.Account;
import com.poly.entity.Item;
import com.poly.entity.Order;
import com.poly.entity.OrderItem;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Service
public class OrderItemService {
	@Autowired
	private OrdersDao ordersRepository;

	@Autowired
	private OrderItemDao orderItemsRepository;

	@Autowired
	AccountDao dao;

	@Autowired
	HttpServletRequest request;
	@Autowired
	SessionService session;
	public void addToCart(Item item) {

		// Lưu thông tin đơn hàng vào bảng "Orders"
		Order order = new Order();
		order.setOrderDate(new Date());// Ngày đặt hàng
		order.setTotalPrice(0); // Giá tổng cộng (tạm thời lấy giá 0)
		// Lấy thông tin người dùng đã đăng nhập từ session
		Integer accountId = (Integer) session.get("accId");
		if (accountId != null) {
			// Truy xuất thông tin tài khoản từ cơ sở dữ liệu bằng accountId
			Account account = dao.findById(accountId).orElse(null);
			if (account != null) {
				// Thêm thông tin tài khoản vào đơn hàng
				order.setAccount(account);
			} 
		} else {
			// Xử lý trường hợp người dùng chưa đăng nhập hoặc session đã hết hạn.

		}

		// Lưu đơn hàng vào cơ sở dữ liệu
		order = ordersRepository.save(order);
		session.set("orderId", order.getOrderId());
		// Lưu thông tin chi tiết món ăn trong đơn hàng vào bảng "OrderItems"
		OrderItem orderItem = new OrderItem();
		orderItem.setOrder(order);
		orderItem.setItem(item);
		orderItem.setQuantity(1); // Số lượng (tạm thời là 1)

		orderItemsRepository.save(orderItem);
	}

}
