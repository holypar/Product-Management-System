package com.product.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.product.entity.Products;
import com.product.repository.ProductRepository;

// basically a router like django

@Controller
public class HomeController {

	@Autowired
	private ProductRepository productRepo;

	@GetMapping("/") // the route
	public String home() {
		return "index"; // the html file name in template dir for the route
	}

	@GetMapping("/load_form")
	public String loadForm() {
		return "add";
	}

	@GetMapping("/edit_form")
	public String editForm() {
		return "edit";
	}

	@PostMapping("/save_products")
	public String saveProducts(@ModelAttribute Products products, HttpSession session) {

		productRepo.save(products);
		session.setAttribute("msg", "Product Added Successfuly");
		return "redirect:/load_form";
	}

}
