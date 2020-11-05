package com.me.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.me.example.exceptions.EntityDoesNotExistException;
import com.me.example.model.Product;
import com.me.example.model.ProductService;

@Transactional
@SpringBootTest
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class DaoTests {
	
	@Autowired
	private ProductService productService;
	
	@Test
	public void productService_saveTest() throws Exception {

		Product prod = createAndPersistTestProduct();

		assertNotNull(prod.getId());
		assertEquals(prod.getName(), "test product");


	}
	
	@Test
	public void productService_findAllTest() throws Exception {
		Product prod = createAndPersistTestProduct();

		List<Product> list = productService.findAll();

		assertFalse(list.isEmpty());
	}
	
	@Test
	public void productService_findByIdTest() throws Exception {
		Product prod = createAndPersistTestProduct();

		Product prod2 = productService.findById(prod.getId());

		assertEquals(prod2.getName(), prod.getName());
	}
	
	@Test
	public void productService_deleteByIdTest() throws Exception {
		Product prod = createAndPersistTestProduct();

		productService.delete(prod.getId());

		assertThrows(EntityDoesNotExistException.class, () -> {
			productService.findById(prod.getId());
			}
		);
	}
		
	private Product createAndPersistTestProduct() {
		Product prod = new Product("test product", 3.33, 10);
		prod.setCategories(Lists.newArrayList("Laptops"));

		prod = productService.save(prod);
		
		return prod;
	}

}
