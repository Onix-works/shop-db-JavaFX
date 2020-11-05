package com.me.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import com.me.example.model.Product;
import com.me.example.model.ProductService;
import com.me.example.supplementary.ProdBean;

@SpringBootTest
@DirtiesContext
public class ProdBeanTests {
	
	@InjectMocks
	ProdBean prodBean;
	@Mock
	ProductService productService;
	
	@BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        
    }	
	
	@Test
	public void selectProductTest() {
		Product product = new Product();
		product.setName("test");
		product.setInStock(5);
		product.setPrice(99.99);
		product.setId((long) 1);
		product.setDescription("test");
		product.getCategories().add("test");
		product.getQualities().put("test","test");
		

		prodBean.selectProduct(product);
		assertEquals(prodBean.getCategories().get(0), "test");
	}
	
	@Test
	public void saveProductSelectedTest() {	
		Product product = new Product();
		product.setName("test");
		product.setInStock(5);
		product.setPrice(99.99);
		product.setId((long) 1);
		product.setDescription("test");
		product.getCategories().add("test");
		product.getQualities().put("test","test");
		
		when(productService.findById((long) 1)).thenReturn(product);
		prodBean.selectProduct(product);
		prodBean.saveProductSelected(prodBean);
		assertEquals(product.getCategories().get(0), prodBean.getCategories().get(0));
	}

}
