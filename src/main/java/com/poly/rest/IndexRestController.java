package com.poly.rest;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.poly.dao.ItemDao;
import com.poly.entity.Category;
import com.poly.entity.Item;
import com.poly.service.CategoryService;
import com.poly.service.CategoryServiceImpl;
import com.poly.service.ItemService;

@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class IndexRestController {

    @Autowired
    private CategoryServiceImpl categoryService;
    @Autowired
    ItemService itemService;
    @Autowired
    private ItemDao itemRepository;

    @GetMapping("/categories")
    public List<Category> getCategories() {
        return categoryService.findAll();
    }

    @GetMapping("{itemId}")
    public Item getOne(@PathVariable("itemId") Integer itemId) {
        return itemService.findById(itemId);
    }

    @GetMapping("/items")
    public Page<Item> getItems(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "6") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return itemRepository.findAll(pageRequest);
    }

    @GetMapping("/items/search")
    public Page<Item> searchItems(
            @RequestParam(name = "keyword") String keyword,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "6") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return itemRepository.findByNameContaining(keyword, pageRequest);
    }

    @GetMapping("/items/by-category")
    public Page<Item> getItemsByCategory(
            @RequestParam(name = "categoryId") Integer categoryId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "6") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return itemRepository.findByCategoryCategoryId(categoryId, pageRequest);
    }

}
