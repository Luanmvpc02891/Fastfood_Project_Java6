package com.poly.rest;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.poly.entity.Category;
import com.poly.entity.Item;
import com.poly.service.CategoryService;
import com.poly.service.ItemService;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/index")
public class IndexRestController {
    @Autowired
    private CategoryService categoryService; // Thay thế 'CategoryService' bằng tên dịch vụ thực tế
    @Autowired
    private ItemService itemService; // Thay thế 'ItemService' bằng tên dịch vụ thực tế

    @GetMapping
    public ResponseEntity<List<Item>> getProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam(name = "cateId", required = false) Integer cateId,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "minPrice", required = false) BigDecimal minPrice,
            @RequestParam(name = "maxPrice", required = false) BigDecimal maxPrice) {

        List<Category> cates = categoryService.findAll();
        List<Item> items;

        if (cateId != null) {
            items = itemService.getItemsByCategory(cateId);
        } else {
            Page<Item> itemsPage = itemService.getItemsByPage(page, size);
            items = itemsPage.getContent();
        }

        if (keyword != null || minPrice != null || maxPrice != null) {
            items = itemService.searchItemsByNameAndPrice(keyword, minPrice, maxPrice);
        }

        return ResponseEntity.ok(items);
    }
}
