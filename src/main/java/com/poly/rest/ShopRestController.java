package com.poly.rest;

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
import com.poly.entity.ItemDetailResponse;
import com.poly.service.CategoryServiceImpl;
import com.poly.service.ItemService;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/shop")
public class ShopRestController {

    @Autowired
    private ItemDao itemRepository;

    @Autowired
    private ItemService itemService;

    @Autowired
    private CategoryServiceImpl categoryService;

    // @GetMapping("{itemId}")
    // public ResponseEntity<Map<String, Object>>
    // getItemDetails(@PathVariable("itemId") Integer itemId) {
    // Item item = itemService.findById(itemId);
    // if (item == null) {
    // return ResponseEntity.notFound().build();
    // }

    // List<Item> relatedProducts = itemService.getRelateditemsExcludingCurrent(
    // item.getCategory().getCategoryId(), itemId);

    // Map<String, Object> response = new HashMap<>();
    // response.put("item", item);
    // response.put("relatedProducts", relatedProducts);

    // return ResponseEntity.ok(response);
    // }

    // @GetMapping("{itemId}")
    // public Item getOne(@PathVariable("itemId") Integer itemId) {
    // return itemService.findById(itemId);
    // }

    // @GetMapping("/items")
    // public Page<Item> getItems(
    // @RequestParam(name = "page", defaultValue = "0") int page,
    // @RequestParam(name = "size", defaultValue = "6") int size) {
    // PageRequest pageRequest = PageRequest.of(page, size);
    // return itemRepository.findAll(pageRequest);
    // }

    @GetMapping("/items")
    public List<Item> getAllItem() {
        return itemRepository.findAll();
    }

    @GetMapping("/categories")
    public List<Category> getCategories() {
        return categoryService.findAll();
    }

    @GetMapping("/items/by-category")
    public Page<Item> getItemsByCategory(
            @RequestParam(name = "categoryId") Integer categoryId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "6") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return itemRepository.findByCategoryCategoryId(categoryId, pageRequest);
    }

    @GetMapping("/items/search")
    public Page<Item> searchItems(
            @RequestParam(name = "keyword") String keyword,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "6") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return itemRepository.findByNameContaining(keyword, pageRequest);
    }

    @GetMapping("/item/{itemId}")
    public ResponseEntity<ItemDetailResponse> getItemDetail(@PathVariable int itemId) {
        Item item = itemService.findById(itemId);
        if (item == null) {
            return ResponseEntity.notFound().build();
        }

        List<Item> relatedProducts = itemService.getRelateditemsExcludingCurrent(
                item.getCategory().getCategoryId(), itemId);

        ItemDetailResponse response = new ItemDetailResponse();
        response.setItem(item);
        response.setRelatedProducts(relatedProducts);

        return ResponseEntity.ok(response);
    }
}
