package com.me.example.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.Data;

@Entity
@Data
public class Product  {
	
	public Product() {
		this.name = "";
		this.price = (double) 0;
		this.inStock = 0;
		this.description = "";
		this.qualities = new HashMap<String, String>();
		this.categories = new ArrayList<String>();
		this.productImage = new ProductImage();
	}
	
	public Product(String name, double price, int inStock){
		this.name = name;
		this.price = price;
		this.inStock = inStock;
		this.description = "";
		this.qualities = new HashMap<String, String>();
		this.categories = new ArrayList<String>();
		this.productImage = new ProductImage();
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	private Double price;
	
	private Integer inStock;
	
	@Column(length = 4000)
	private String description;
	
	@ElementCollection(fetch = FetchType.EAGER)
	Map<String, String> qualities;
	
	@ElementCollection(fetch = FetchType.EAGER)
	List<String> categories;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	ProductImage productImage;

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", price=" + price + ", inStock=" + inStock + "]";
	}
}