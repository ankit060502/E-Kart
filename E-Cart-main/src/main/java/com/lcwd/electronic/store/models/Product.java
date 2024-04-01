package com.lcwd.electronic.store.models;

import java.sql.Date;

import org.hibernate.Length;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "products")
public class Product {
	
	@Id
	private String productId;
	
	private String title;
	
	
	private String description;
	private String brandName;
	private String productImage;
	
	private String color;
	private int price;
	private int discount;
	private int quantity;
	private Date addDate;
	private boolean live;
	private boolean stock;
	
	
	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;
	
	

}
