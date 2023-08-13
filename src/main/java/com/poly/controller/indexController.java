package com.poly.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.poly.dao.CategoryDao;
import com.poly.dao.ItemDao;
import com.poly.entity.Category;
import com.poly.entity.Item;
import com.poly.service.CategoryService;
import com.poly.service.CategoryServiceImpl;
import com.poly.service.ItemService;

@Controller
public class indexController {
	@Autowired
	private ItemService itemService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private CategoryServiceImpl categoryServiceImpl;

	// @GetMapping({ "/index", "/" })
	// public String index(Model model, @RequestParam(defaultValue = "1") int page,
	// @RequestParam(defaultValue = "3") int size,
	// @RequestParam(name = "cateId", required = false) Integer cateId,
	// @RequestParam(name = "keyword", required = false) String keyword,
	// @RequestParam(name = "minPrice", required = false) BigDecimal minPrice,
	// @RequestParam(name = "maxPrice", required = false) BigDecimal maxPrice) {

	// // Lấy danh sách các loại sản phẩm và đưa vào model
	// List<Category> cates = categoryServiceImpl.findAll();

	// model.addAttribute("cates", cates);

	// // Biến để kiểm tra có sản phẩm hay không
	// boolean hasItems = true;
	// List<Item> items;

	// // Kiểm tra nếu có tham số cateId được truyền vào, lấy danh sách sản phẩm
	// theo
	// // ID của loại
	// if (cateId != null) {
	// items = itemService.getItemsByCategory(cateId);
	// } else {
	// // Nếu không có tham số cateId, lấy danh sách tất cả sản phẩm
	// Page<Item> itemsPage = itemService.getItemsByPage(page, size);
	// items = itemsPage.getContent();
	// model.addAttribute("currentPage", page);
	// int totalPages = (int) Math.ceil((double) itemsPage.getTotalElements() /
	// size);
	// if (totalPages < 1) {
	// totalPages = 1;
	// }
	// model.addAttribute("totalPages", totalPages);
	// }

	// // Kiểm tra nếu có tham số keyword hoặc minPrice hoặc maxPrice được truyền
	// vào,
	// // thực hiện tìm kiếm và lấy danh sách sản phẩm phù hợp
	// if (keyword != null || minPrice != null || maxPrice != null) {
	// items = itemService.searchItemsByNameAndPrice(keyword, minPrice, maxPrice);
	// }

	// // Kiểm tra có sản phẩm hay không
	// hasItems = !items.isEmpty();
	// model.addAttribute("items", items);
	// model.addAttribute("hasItems", hasItems);

	// return "index";
	// }

	@GetMapping({ "/index", "/" })
	public String index2(Model model) {
		return "index";
	}

	@GetMapping("/cart")
	public String cart(Model model) {
		return "cart";
	}

	@GetMapping("/checkout")
	public String checkout(Model model) {
		return "checkout";
	}

	@GetMapping("/contact")
	public String contact(Model model) {
		return "contact";
	}

	@GetMapping("/about")
	public String about(Model model) {
		return "about";
	}

	@GetMapping("/news")
	public String news(Model model) {
		return "news";
	}

	@GetMapping("/single-news")
	public String singlenews(Model model) {
		return "single-news";
	}

	@GetMapping("/single-product/{itemId}")
	public String singleproduct(Model model) {
		return "single-product";
	}

}
