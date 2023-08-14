package com.poly.restAdmin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.poly.dao.AccountDao;
import com.poly.dao.RoleDAO;
import com.poly.entity.Account;
import com.poly.entity.Role;

@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class AccountRestController {
    @Autowired
    private AccountDao accountRepository; // Đảm bảo bạn đã inject repository này
    @Autowired
    RoleDAO roledao;

    @Value("${image.upload.directory}") // Đường dẫn thư mục lưu ảnh, đọc từ cấu hình
    private String imageUploadDirectory;

    @GetMapping("/roleall")
    public List<Role> getAllRole() {
        return roledao.findAll();
    }

    @GetMapping("/accounts")
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @PostMapping("/accounts")
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        try {
            account.setActive(true); // Thiết lập isActive cho account từ request thành true
            Account newAccount = accountRepository.save(account); // Lưu account vào cơ sở dữ liệu
            return ResponseEntity.status(HttpStatus.CREATED).body(newAccount);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/accounts/{accountId}")
    public ResponseEntity<Account> updateAccount(@PathVariable int accountId, @RequestBody Account updatedAccount) {
        try {
            Account existingAccount = accountRepository.findById(accountId);
            if (existingAccount == null) {
                return ResponseEntity.notFound().build();
            }

            // Cập nhật thông tin từ updatedAccount vào existingAccount
            existingAccount.setFullName(updatedAccount.getFullName());
            existingAccount.setEmail(updatedAccount.getEmail());
            existingAccount.setPassword(updatedAccount.getPassword());
            existingAccount.setUsername(updatedAccount.getUsername());
            existingAccount.setImage(updatedAccount.getImage());
            existingAccount.setPhoneNumber(updatedAccount.getPhoneNumber());
            existingAccount.setAddressCity(updatedAccount.getAddressCity());
            existingAccount.setAddressDistrict(updatedAccount.getAddressDistrict());
            existingAccount.setActive(updatedAccount.isActive() == true);

            Account savedAccount = accountRepository.save(existingAccount);
            return ResponseEntity.ok(savedAccount);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/deactivate/{accountId}")
    public ResponseEntity<Account> deactivateAccount(@PathVariable int accountId) {
        try {
            Account existingAccount = accountRepository.findById(accountId);
            if (existingAccount == null) {
                return ResponseEntity.notFound().build();
            }

            existingAccount.setActive(false); // Cập nhật trạng thái active về false
            Account deactivatedAccount = accountRepository.save(existingAccount);

            return ResponseEntity.ok(deactivatedAccount);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
