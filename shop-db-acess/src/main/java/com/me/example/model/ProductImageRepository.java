package com.me.example.model;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;


public interface ProductImageRepository extends PagingAndSortingRepository<ProductImage, Long> {

}