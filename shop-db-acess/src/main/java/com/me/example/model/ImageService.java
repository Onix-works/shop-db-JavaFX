package com.me.example.model;

import java.util.List;

public interface ImageService {
	List<ProductImage> findAll();
	ProductImage findById(Long id);
	ProductImage save(ProductImage image);
	void delete(Long id);
	ProductImage update(ProductImage image);

}