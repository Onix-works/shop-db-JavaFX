package com.me.example;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.me.example.model.ProductService;
import com.me.example.supplementary.ProdBean;

public class HomeControllerTests {
	
	@InjectMocks
	ProdBean prodBean;
	@Mock
	ProductService productService;
	
	@BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        
    }	

}
