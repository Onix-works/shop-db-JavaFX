package com.me.example.model;

import java.sql.Blob;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.Type;
import org.springframework.transaction.annotation.Transactional;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
public class ProductImage {
	
	
	public ProductImage() {
		this.photo = new byte[0];
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Type(type = "org.hibernate.type.ImageType")
	@Basic(fetch= FetchType.LAZY)
	private byte[] photo;
	
}
