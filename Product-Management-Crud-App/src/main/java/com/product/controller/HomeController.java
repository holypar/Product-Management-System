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
		int productValue = 0;
		int productCount = 0;
		int totalValue = 0;
		int totalCount = 0;
		for (int i = 0; i < list.size(); i++) {
			productValue = Integer.parseInt(list.get(i).getPrice());
			productCount = Integer.parseInt(list.get(i).getQuantity());
			totalCount += productCount;
			totalValue += (productValue * productCount);
		}
		m.addAttribute("totalValue", totalValue);
		m.addAttribute("productCount", totalCount);
		return "home";
	}

	@GetMapping("/error")
	public String loadError() {
		return "redirect:/search";
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

	@PostMapping("/update_products/{id}")
	public String updateProducts(@PathVariable(value = "id") long id, @ModelAttribute Products products,
			HttpSession session) {

		productRepo.save(products);

		String s = String.valueOf(id);
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
		if (pids == null) {
			return "redirect:/search";
		}
		productRepo.bulkDelete(pids);
		return "redirect:/search";

	}

}
