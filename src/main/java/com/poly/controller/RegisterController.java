package com.poly.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.poly.entity.Account;
import com.poly.repository.UserRepository;

import jakarta.validation.Valid;

@Controller
public class RegisterController {

	@Autowired
	UserRepository RepoUser;
	
	@GetMapping("/dangky")
	public String showRegisterForm(Model model) {
		model.addAttribute("account", new Account());
		return "dangky";
	}
	
	@PostMapping("/dangky")
	public String usermanagement_create(@Valid @ModelAttribute("account") Account account, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "dangky"; // Trả
		} else {
			account.setActive(true);
			account.setRole(null);
			RepoUser.save(account);
			model.addAttribute("message", "Create success ");
			return "login";
		}
	}
}
