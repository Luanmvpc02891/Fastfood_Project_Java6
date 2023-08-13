package com.poly.restAdmin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.dao.CategoryDao;
import com.poly.entity.Account;
import com.poly.entity.Category;
import com.poly.entity.Item;

@CrossOrigin("*")
@RestController
@RequestMapping("/category")
public class CategoryRestController {
    @Autowired
    CategoryDao dao;

    @PostMapping("/cate")
    public ResponseEntity<Category> Create(@RequestBody Category cate) {
        try {
            cate.setActive(true); // Thiết lập isActive cho item từ request thành true
            Category newCate = dao.save(cate); // Lưu item vào cơ sở dữ liệu
            return ResponseEntity.status(HttpStatus.CREATED).body(newCate);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/cate/{categoryId}")
    public ResponseEntity<Category> update(@PathVariable int categoryId, @RequestBody Category updatedCate) {
        try {
            Category existingCate = dao.findById(categoryId);
            if (existingCate == null) {
                return ResponseEntity.notFound().build();
            }

            // Cập nhật thông tin từ updatedCate vào existingCate
            existingCate.setName(updatedCate.getName());
            existingCate.setDescription(updatedCate.getDescription());
            existingCate.setActive(updatedCate.isActive() == true);

            Category savedCate = dao.save(existingCate);
            return ResponseEntity.ok(savedCate);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/deactivate/{categoryId}")
    public ResponseEntity<Category> deactivate(@PathVariable int categoryId) {
        try {
            Category existingItem = dao.findById(categoryId);
            if (existingItem == null) {
                return ResponseEntity.notFound().build();
            }

            existingItem.setActive(false); // Cập nhật trạng thái active về false
            Category deactivatedItem = dao.save(existingItem);

            return ResponseEntity.ok(deactivatedItem);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
