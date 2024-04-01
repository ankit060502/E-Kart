package com.lcwd.electronic.store.dtos;



import java.util.Date;

import com.lcwd.electronic.store.models.Category;
import com.lcwd.electronic.store.models.Product;

import jakarta.persistence.Id;
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
public class ProductDto {

	
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
	private CategoryDto category;
	
}
