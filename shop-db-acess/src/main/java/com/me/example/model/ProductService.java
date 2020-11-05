package com.me.example.model;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
	List<Product> findAll();
	Product findById(Long id);
	Product findByName(String name);
	Product save(Product product);
	void delete(Long id);
	Product update(Product product);
	Page<Product> findAllByPage(Pageable pageable);
	Page<Product> findByCategories(String name, Pageable p);
	Page<Product> findByNameContaining(String name, Pageable p);
	Page<Product> findByByIdContaining(String id, Pageable pageable);
}
