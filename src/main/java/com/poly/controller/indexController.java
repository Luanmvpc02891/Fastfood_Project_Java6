package com.poly.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.poly.entity.Account;
import com.poly.repository.UserRepository;
import com.poly.service.SessionService;
import com.poly.config.SecurityConfig;
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
	UserRepository dao;
	
	
	@Autowired
	SecurityConfig secConfig;
	

	@RequestMapping("/index")
	public String index(Model model) {
		return "index";
	}
	
	
	
	
	@RequestMapping("/checkout")
	public String checkout(Model model) {
		return "checkout";
	}
	
	@RequestMapping("/contact")
	public String contact(Model model) {
		return "contact";
	}
	
	@RequestMapping("/about")
	public String about(Model model) {
		return "about";
	}
	
	@RequestMapping("/news")
	public String news(Model model) {
		return "news";
	}
	
	@RequestMapping("/single-news")
	public String singlenews(Model model) {
		return "single-news";
	}
	
	@RequestMapping("/single-product")
	public String singleproduct(Model model) {
		return "single-product";
	}
	
	@RequestMapping("/profile")
	public String indexAdmin(Model model) {
		return "admin/profile";
	}
	
	@RequestMapping("/auth/dangnhap/form")
	public String login(Model model) {
		model.addAttribute("account", new Account());
		return "/auth/dangnhap";
	}
	
	@RequestMapping(value = "/auth/dangnhap/error")
	public String loginFail(Model model) {
		model.addAttribute("message", "Sai tên đăng nhập hoặc mật khẩu");
		System.out.println("Error");
		return "forward:/auth/dangnhap/form";
	}

	
	@RequestMapping("/auth/dangnhap/success")
	public String loginProcessing(Model model) {
		System.out.println("Login Local");
		model.addAttribute("account", new Account());
		return "redirect:/index";
	}
	@RequestMapping("/admin")
	public String adminPage(Model model) {
		model.addAttribute("account", new Account());
		return "admin/index2";
	}
	
	@RequestMapping("/rest/products")
	public String cartProducts(Model model) {
		model.addAttribute("account", new Account());
		return "/cart";
	}
	
	@RequestMapping("/cart")
	public String cart(Model model) {
		model.addAttribute("account", new Account());
		return "/cart";
	}
	
	@RequestMapping("/auth/dangnhap/denied")
	public String denied(Model model) {
		System.out.println("không có quyền truy cập");
		model.addAttribute("message","không có quyền truy cập");
		return "redirect:/index";
	}
	
	

}
