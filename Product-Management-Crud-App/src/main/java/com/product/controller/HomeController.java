package com.product.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.product.entity.Products;
import com.product.service.ProductService;

// basically a router like django

@Controller
public class HomeController {

	@Autowired
	private ProductService productRepo; // allows me to use JPA
//	private ProductService service;

	@GetMapping("/")
	public String loadHome(Model m) {
		List<Products> list = productRepo.findAll("");
		int totalValue = 0;
		int productCount = list.size();
		for (int i = 0; i < list.size(); i++) {
			totalValue += Integer.parseInt(list.get(i).getPrice());
		}
		m.addAttribute("totalValue", totalValue);
		m.addAttribute("productCount", productCount);
		return "home";
	}

	@GetMapping("/search") // the route
	public String home(Model m, @Param("keyword") String keyword) {
		List<Products> list = productRepo.findAll(keyword);
		m.addAttribute("all_products", list); // when we call the list in html call it by all_products
		// add total inventory value here
		// add total # of prods

		return "index"; // the html file name in template dir for the route
	}

	@GetMapping("/load_form")
	public String loadForm() {
		return "add";
	}

	@GetMapping("/edit_form/{id}")
	public String editForm(@PathVariable(value = "id") long id, Model m) {
		Optional<Products> product = Optional.ofNullable(productRepo.findById(id));
		Products pro = product.get();
		m.addAttribute("product", pro);
		return "edit";
	}

	@GetMapping("/detail/{id}")
	public String detailForm(@PathVariable(value = "id") long id, Model m) {
		Optional<Products> product = Optional.ofNullable(productRepo.findById(id));
		Products pro = product.get();
		m.addAttribute("product", pro);
		return "detail";
	}

	@PostMapping("/save_products")
	public String saveProducts(@ModelAttribute Products products, HttpSession session) {

		productRepo.save(products);
		List<Products> list = productRepo.findAll("");
		Products lastAdded = list.get(list.size() - 1);
		long lastId = lastAdded.getId();
		String s = String.valueOf(lastId);
		session.setAttribute("msg", "Product ADDED Successfuly");
		return "redirect:/detail/" + s;
	}

	@PostMapping("/update_products")
	public String updateProducts(@ModelAttribute Products products, HttpSession session) {

		productRepo.save(products);
		List<Products> list = productRepo.findAll("");
		Products lastAdded = list.get(list.size() - 1);
		long lastId = lastAdded.getId();
		String s = String.valueOf(lastId);
		session.setAttribute("msg", "Product UPDATED Successfuly");
		return "redirect:/detail/" + s;
	}

	@GetMapping("/delete/{id}")
	public String deleteProducts(@PathVariable(value = "id") long id, HttpSession session) {
		productRepo.deleteById(id);
		session.setAttribute("msg", "Product DELETED Successfuly");
		return "redirect:/search";

	}

	@GetMapping("/bulk_delete")
	public String deleteBulk(@RequestParam("pid") Long[] pids) {
		productRepo.bulkDelete(pids);
		return "redirect:/search";

	}

}
