package com.poly.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.poly.dao.AccountDao;
import com.poly.entity.Account;
import com.poly.service.SessionService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {
	@Autowired
	AccountDao dao;

	@GetMapping("/login")
	public String showLoginForm(Model model) {
		model.addAttribute("acc", new Account());
		return "login";
	}

	@PostMapping("/login")
	public String login(@ModelAttribute("acc") Account acc, @RequestParam("username") String username,
			@RequestParam("password") String password, Model model, HttpSession session) {
		acc = dao.findByUsername(username);
		if (acc != null && acc.getPassword().equals(password)) {
			session.setAttribute("accId", acc.getAccountId());
			return "redirect:/index";
		}
		model.addAttribute("messages", "Tài khoản của bạn không đúng.");
		return "login";
	}
}
