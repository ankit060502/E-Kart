package com.lcwd.electronic.store.services;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.ProductDto;

public interface ProductService {
	
	
	//create product
	
	ProductDto createProduct(ProductDto productDto);
	
	// update product
	
	ProductDto updateProduct(ProductDto productDto,String productId);
	
	// get all product
	PageableResponse<ProductDto> getAllProduct(int pageNumber,int pageSize,String sortedBy,String sortedDir);
	
	// delete product
	
	void deleteProduct(String productId);
	
	//get single product
	
	ProductDto getProductById(String productId);
	
	// get all  Product by given title
	PageableResponse<ProductDto>  getByTitle(String subTitle,int pageNumber,int pageSize,String sortBy,String sortDir);
	
	
	
	
	//get all live	
	PageableResponse<ProductDto>  getAllLive(int pageNumber,int pageSize,String sortBy,String sortDir);
	
	
	
	
	
	
	
	// creating  product with category 
	
	ProductDto createProductWithCategory(ProductDto productDto ,String categoryId);
	
	
	//assigning product with given id
	ProductDto assignProduct(String productId,String categoryId);
	
	
	//  getting all product of given category 
	
	PageableResponse<ProductDto> getAllProductofCategory(String categoryId,int pageNumber,int pageSize,String sortBy,String sortDir);
	
	
	
	
	

}
