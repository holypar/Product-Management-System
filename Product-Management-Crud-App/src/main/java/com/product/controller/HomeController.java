package com.product.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.product.entity.Products;
import com.product.repository.ProductRepository;

// basically a router like django

@Controller
public class HomeController {

	@Autowired
	private ProductRepository productRepo;

	@GetMapping("/")
	public String loadHome() {
		return "home";
	}

	@GetMapping("/search") // the route
	public String home(Model m) {
		List<Products> list = productRepo.findAll();
		m.addAttribute("all_products", list); // when we call the list in html call it by all_products

		return "index"; // the html file name in template dir for the route
	}

	@GetMapping("/load_form")
	public String loadForm() {
		return "add";
	}

	@GetMapping("/edit_form/{id}")
	public String editForm(@PathVariable(value = "id") long id, Model m) {
		Optional<Products> product = productRepo.findById(id);
		Products pro = product.get();
		m.addAttribute("product", pro);
		return "edit";
	}

	@GetMapping("/detail/{id}")
	public String detailForm(@PathVariable(value = "id") long id, Model m) {
		Optional<Products> product = productRepo.findById(id);
		Products pro = product.get();
		m.addAttribute("product", pro);
		return "detail";
	}

	@PostMapping("/save_products")
	public String saveProducts(@ModelAttribute Products products, HttpSession session) {

		productRepo.save(products);
		List<Products> list = productRepo.findAll();
		Products lastAdded = list.get(list.size() - 1);
		long lastId = lastAdded.getId();
		String s = String.valueOf(lastId);
		session.setAttribute("msg", "Product Added Successfuly");
		return "redirect:/detail/" + s;
	}

	@PostMapping("/update_products")
	public String updateProducts(@ModelAttribute Products products, HttpSession session) {

		productRepo.save(products);
		session.setAttribute("msg", "Product Updated Successfuly");
		return "redirect:/search";
	}

	@GetMapping("/delete/{id}")
	public String deleteProducts(@PathVariable(value = "id") long id, HttpSession session) {
		productRepo.deleteById(id);
		session.setAttribute("msg", "Product Deleted Successfuly");

		return "redirect:/search";

	}

}
