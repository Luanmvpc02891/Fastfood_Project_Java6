package com.poly.rest;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.poly.dao.AccountDao;
import com.poly.dao.ItemDao;
import com.poly.dao.OrderItemDao;
import com.poly.dao.OrdersDao;
import com.poly.entity.Account;
import com.poly.entity.Category;
import com.poly.entity.Item;
import com.poly.entity.Order;
import com.poly.entity.OrderItem;
import com.poly.service.CategoryServiceImpl;
import com.poly.service.ItemService;
import com.poly.service.OrderService;
import com.poly.service.SessionService;

import jakarta.servlet.http.HttpSession;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/products")
public class ItemRestController {
	@Autowired
	ItemService itemService;

	@Autowired
	OrdersDao orderDao;

	@Autowired
	ItemDao itemDao;

	@Autowired
	OrderItemDao orderItemDao;

	@Autowired
	SessionService session;

	@Autowired
	AccountDao dao;

	@Autowired
	OrderService orderService;

	@Autowired
	private CategoryServiceImpl categoryService;

	@GetMapping("{itemId}")
	public Item getOne(@PathVariable("itemId") Integer itemId) {
		return itemService.findById(itemId);
	}

	@GetMapping("/cart-items")
	public List<OrderItem> getCartItems(HttpSession session) {
		Integer accountId = (Integer) session.getAttribute("accountId");

		// Lấy danh sách sản phẩm trong giỏ hàng dựa trên accountId
		List<OrderItem> cartItems = orderItemDao.findByOrder_Account_AccountId(accountId);
		return cartItems;
	}

	@PostMapping("/add-to-cart/{itemId}/{accountId}/{quantity}")
	public ResponseEntity<String> addToCart(@PathVariable("itemId") Integer itemId,
			@PathVariable("accountId") Integer accountId,
			@PathVariable("quantity") int quantity) {
		Item item = itemService.findById(itemId);
		Account account = dao.findById(accountId).get();

		if (item == null || account == null) {
			return ResponseEntity.badRequest().body("Item or account not found!");
		}

		// Tạo đơn hàng mới hoặc lấy đơn hàng chưa hoàn tất của người dùng
		Order order = orderDao.findFirstByAccountAndTotalPrice(account, 0.0);
		if (order == null) {
			order = new Order();
			order.setOrderDate(new Date());
			order.setTotalPrice(0);
			order.setAccount(account);
			// Thiết lập các thông tin khác cho đơn hàng (ngày đặt hàng, tổng tiền,...)

			order = orderDao.save(order);
		}
		// Đếm số lượng sản phẩm trong đơn hàng
		int itemCount = orderItemDao.countByOrderAccountAndItem(account, item);

		// Kiểm tra xem sản phẩm đã có trong đơn hàng chưa
		OrderItem existingItem = orderItemDao.findByOrderAndItem(order, item);
		if (existingItem != null) {
			existingItem.setQuantity(existingItem.getQuantity() + 1);
			orderItemDao.save(existingItem);
		} else {
			// Thêm sản phẩm vào đơn hàng
			OrderItem orderItem = new OrderItem();
			orderItem.setOrder(order);
			orderItem.setItem(item);
			orderItem.setQuantity(quantity); // Số lượng sản phẩm được chuyển từ Angular
			// Thiết lập các thông tin khác cho chi tiết đơn hàng
			orderItemDao.save(orderItem);
			return ResponseEntity.ok().body("{\"message\": \"Sản phẩm đã thêm trong giỏ hàng!!!\"}");
		}
		return ResponseEntity.ok().body("{\"message\": \"Sản phẩm đã thêm trong giỏ hàng!!!\"}");
	}

	@DeleteMapping("/remove-from-cart/{itemId}/{accountId}")
	public ResponseEntity<String> removeFromCart(@PathVariable("itemId") Integer itemId,
			@PathVariable("accountId") Integer accountId) {

		Item item = itemService.findById(itemId);
		Account account = dao.findById(accountId).orElse(null);

		if (item == null || account == null) {
			return ResponseEntity.badRequest().body("Item or account not found!");
		}

		// Tìm giỏ hàng chưa hoàn tất của người dùng
		Order order = orderDao.findFirstByAccountAndTotalPrice(account, 0.0);
		if (order != null) {
			// Xóa sản phẩm khỏi giỏ hàng
			OrderItem orderItem = orderItemDao.findByOrderAndItem(order, item);
			if (orderItem != null) {
				orderItemDao.delete(orderItem);
				return ResponseEntity.ok().body("{\"message\": \"Item removed from cart!\"}");

			}
		}

		return ResponseEntity.ok().body("{\"message\": \"Item removed from cart!\"}");

	}

	@PutMapping("/update-quantity/{itemId}/{quantity}/{accountId}")
	public ResponseEntity<String> updateCartItemQuantity(@PathVariable("itemId") Integer itemId,
			@PathVariable("quantity") int quantity, @PathVariable("accountId") Integer accountId) {

		Item item = itemService.findById(itemId);
		Account account = dao.findById(accountId).orElse(null);
		if (item == null) {
			return ResponseEntity.badRequest().body("Item not found!");
		}

		// Tìm giỏ hàng chưa hoàn tất của người dùng

		Order order = orderDao.findFirstByAccountAndTotalPrice(account, 0.0);
		if (order != null) {
			// Xác định chi tiết đơn hàng tương ứng với sản phẩm và cập nhật số lượng
			OrderItem orderItem = orderItemDao.findByOrderAndItem(order, item);
			if (orderItem != null) {
				orderItem.setQuantity(quantity);
				orderItemDao.save(orderItem);
				return ResponseEntity.ok().body("{\"message\": \"Đã cập nhật số lượng!\"}");
			}
		}

		return ResponseEntity.ok().body("{\"message\": \"Đã cập nhật số lượng!\"}");

	}

	@GetMapping("/categories")
	public List<Category> getCategories() {
		return categoryService.findAll();
	}

	@GetMapping("/items/by-category")
	public Page<Item> getItemsByCategory(
			@RequestParam(name = "categoryId") Integer categoryId,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "3") int size) {
		PageRequest pageRequest = PageRequest.of(page, size);
		return itemDao.findByCategoryCategoryId(categoryId, pageRequest);
	}

}
