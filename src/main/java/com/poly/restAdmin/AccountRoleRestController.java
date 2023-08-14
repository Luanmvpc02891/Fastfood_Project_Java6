package com.poly.restAdmin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.dao.AccountRoleDAO;
import com.poly.entity.Account;
import com.poly.entity.AccountRoles;
import com.poly.entity.Category;
import com.poly.entity.Item;

@CrossOrigin("*")
@RestController
@RequestMapping("/account")
public class AccountRoleRestController {
    @Autowired
    private AccountRoleDAO dao;

    @GetMapping("/role")
    public List<AccountRoles> getAllAccounts() {
        return dao.findAll();
    }

    @PostMapping("/role")
    public ResponseEntity<?> create(@RequestBody AccountRoles item) {
        try {
            // Kiểm tra nếu cặp (accountId, roleId) đã tồn tại
            AccountRoles existingItem = dao.findByAccountAndRole(item.getAccount(), item.getRole());
            if (existingItem != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Cặp (accountId, roleId) đã tồn tại");
            }

            // Nếu không trùng, lưu vào cơ sở dữ liệu
            AccountRoles newItem = dao.save(item);
            return ResponseEntity.status(HttpStatus.CREATED).body(newItem);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/role/update/{accountRolesId}")
    public ResponseEntity<AccountRoles> update(@PathVariable int accountRolesId,
            @RequestBody AccountRoles updatedCate) {
        try {
            AccountRoles existingCate = dao.findById(accountRolesId);
            if (existingCate == null) {
                return ResponseEntity.notFound().build();
            }

            // Cập nhật thông tin từ updatedCate vào existingCate
            existingCate.setAccount(updatedCate.getAccount());
            existingCate.setRole(updatedCate.getRole());

            AccountRoles savedCate = dao.save(existingCate);
            return ResponseEntity.ok(savedCate);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/role/delete/{accountRolesId}")
    public ResponseEntity<String> delete(@PathVariable int accountRolesId) {
        try {
            AccountRoles existingCate = dao.findById(accountRolesId);
            if (existingCate == null) {
                return ResponseEntity.notFound().build();
            }

            dao.delete(existingCate);
            return ResponseEntity.ok().body("{\"message\": \"Xóa thành công!\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}