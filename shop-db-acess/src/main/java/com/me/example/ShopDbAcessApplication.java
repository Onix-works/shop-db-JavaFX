package com.me.example;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import com.me.example.supplementary.ProdBean;

import javafx.application.Application;

@SpringBootApplication
public class ShopDbAcessApplication {

	public static void main(String[] args) {
		Application.launch(JavaFxApplication.class, args);

	}
	
	@Bean
	@Scope("singleton")
    public ProdBean productSelected() {
		   return new ProdBean();
	}
	
}
	
