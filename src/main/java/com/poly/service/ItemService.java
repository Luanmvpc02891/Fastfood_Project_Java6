package com.poly.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.poly.dao.AccountDao;
import com.poly.dao.ItemDao;
import com.poly.entity.Account;
import com.poly.entity.Item;

@Service
public class ItemService {

	@Autowired
	private ItemDao itemDao;
	@Autowired
	AccountDao dao;

	public Page<Item> getItemsByPage(int pageNumber, int pageSize) {
		Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
		return itemDao.findAll(pageable);
	}

	public Item findById(Integer itemId) {
		// Implement code để lấy thông tin sản phẩm dựa trên productId từ cơ sở dữ liệu
		// Ví dụ:
		return itemDao.findById(itemId).orElse(null);
	}

	public List<Item> getRelateditemsExcludingCurrent(Integer categoryId, Integer currentItemId) {
		// Implement code để lấy danh sách sản phẩm cùng loại trừ sản phẩm có
		// currentProductId từ cơ sở dữ liệu
		// Ví dụ:
		return itemDao.findByCategory_CategoryIdAndItemIdNot(categoryId, currentItemId);
	}

	public List<Item> getItemsByCategory(Integer categoryId) {
		// Gọi phương thức từ ItemRepository để lấy danh sách sản phẩm của loại
		// categoryId
		return itemDao.findByCategoryCategoryId(categoryId);
	}

	public List<Item> searchItemsByNameAndPrice(String keyword, BigDecimal minPrice, BigDecimal maxPrice) {
		// Nếu không có từ khóa hoặc giá cả xác định, trả về tất cả sản phẩm
		if (keyword == null && (minPrice == null || maxPrice == null)) {
			return itemDao.findAll();
		}

		// Nếu có từ khóa, nhưng giá không xác định, tìm kiếm theo tên sản phẩm
		if (keyword != null && (minPrice == null || maxPrice == null)) {
			return itemDao.findByNameContainingIgnoreCase(keyword);
		}

		// Nếu có giá cả xác định, nhưng từ khóa không có, tìm kiếm theo khoảng giá
		if (minPrice != null && maxPrice != null && keyword == null) {
			return itemDao.findByPriceBetween(minPrice, maxPrice);
		}

		// Nếu cả từ khóa và giá cả đều có, tìm kiếm theo cả hai tiêu chí
		return itemDao.findByNameContainingIgnoreCaseAndPriceBetween(keyword, minPrice, maxPrice);
	}

}
