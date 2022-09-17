package com.product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.product.entity.Products;

@Repository
public interface ProductRepository extends JpaRepository<Products, Long> {

	@Query("SELECT p FROM Products p WHERE CONCAT(p.productName) LIKE %?1%")
	public List<Products> findAll(String keyword);

}
