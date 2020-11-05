package com.me.example.model;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {

	public Optional<Product> findByName(String name);

	public Page<Product> findByCategories(String name, Pageable p);

	@Override
	void delete(Product product);

	public Page<Product> findByNameContaining(String name, Pageable p);

	@Query(value = "SELECT m FROM Product m WHERE CAST( m.id AS string) LIKE %:data%",
			countQuery = "SELECT count(m) FROM Product m WHERE CAST( m.id AS string) LIKE %:data%")
	public Page<Product> findByByIdContaining(@Param("data") String data, Pageable pageable);

}