package com.poly.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.poly.dao.ItemDao;
import com.poly.entity.Item;
import org.springframework.web.bind.annotation.RequestParam;
import com.poly.entity.Category;
import com.poly.service.CategoryServiceImpl;
import com.poly.service.ItemService;

@Controller
public class shopController {
	@Autowired
	ItemDao dao;
	@Autowired
	private ItemService itemService;

	@Autowired
	private CategoryServiceImpl categoryServiceImpl;

	@GetMapping("/shop")
	public String shop(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "3") int size,
			@RequestParam(name = "cateId", required = false) Integer cateId, Model model) {
		// Lấy danh sách các loại sản phẩm và đưa vào model
		List<Category> cates = categoryServiceImpl.findAll();
		model.addAttribute("cates", cates);

		return "shop";
	}

	// @GetMapping("/shop/{itemId}")
	// public String detal(@PathVariable int itemId, Model model) {
	// Item item = dao.findById(itemId).orElse(null);
	// model.addAttribute("items", item);
	// // Lấy danh sách sản phẩm cùng loại trừ sản phẩm đang hiển thị

	// List<Item> relatedProducts =
	// itemService.getRelateditemsExcludingCurrent(item.getCategory().getCategoryId(),
	// itemId);
	// model.addAttribute("itemss", relatedProducts);
	// return "single-product";
	// }
}
