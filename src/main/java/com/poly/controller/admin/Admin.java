package com.poly.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Admin {
	@GetMapping("profile")
	public String indexAdmin(Model model) {
		return "admin/profile";
	}

	@GetMapping("category")
	public String index2(Model model) {
		return "admin/basic-table";
	}

	@GetMapping("admin")
	public String index3(Model model) {
		return "admin/dashboard";
	}

	@GetMapping("item")
	public String index4(Model model) {
		return "admin/item";
	}

	@GetMapping("role")
	public String index5(Model model) {
		return "admin/accountrole";
	}

	@GetMapping("reve")
	public String index6(Model model) {
		return "admin/revenue";
	}
}
