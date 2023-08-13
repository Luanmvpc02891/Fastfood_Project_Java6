package com.poly.restAdmin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.poly.dao.ItemDao;
import com.poly.entity.Account;
import com.poly.entity.Item;

@CrossOrigin("*")
@RestController
@RequestMapping("/product")
public class ItemAdminRestController {
    @Autowired
    private ItemDao dao; // Đảm bảo bạn đã inject repository này

    @GetMapping("/item1")
    public List<Item> getAllItem() {
        return dao.findAll();
    }

    @PostMapping("/item1")
    public ResponseEntity<Item> CreateItem(@RequestBody Item item) {
        try {
            item.setActive(true); // Thiết lập isActive cho item từ request thành true
            Item newitem = dao.save(item); // Lưu item vào cơ sở dữ liệu
            return ResponseEntity.status(HttpStatus.CREATED).body(newitem);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/item1/{itemId}")
    public ResponseEntity<Item> updateItem(@PathVariable int itemId, @RequestBody Item updatedItem) {
        try {
            Item existingItem = dao.findById(itemId);
            if (existingItem == null) {
                return ResponseEntity.notFound().build();
            }
            // Cập nhật thông tin từ updatedItem vào existingItem
            existingItem.setName(updatedItem.getName());
            existingItem.setPrice(updatedItem.getPrice());
            existingItem.setCodeId(updatedItem.getCodeId());
            existingItem.setActive(updatedItem.isActive() == true);
            existingItem.setImage(updatedItem.getImage());
            existingItem.setStatus(updatedItem.isStatus());

            Item savedItem = dao.save(existingItem);
            return ResponseEntity.ok(savedItem);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/deactivate/{itemId}")
    public ResponseEntity<Item> deactivateAccount(@PathVariable int itemId) {
        try {
            Item existingItem = dao.findById(itemId);
            if (existingItem == null) {
                return ResponseEntity.notFound().build();
            }

            existingItem.setActive(false); // Cập nhật trạng thái active về false
            Item deactivatedItem = dao.save(existingItem);

            return ResponseEntity.ok(deactivatedItem);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}