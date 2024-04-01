package com.lcwd.electronic.store.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.lcwd.electronic.store.models.Product;
import com.lcwd.electronic.store.models.Category;




public interface ProductRepository  extends JpaRepository<Product, String>{
	
	
	// search 
	Page<Product>  findByTitleContaining(String subTitle,Pageable pageable);
	
	
	
	Page<Product> findByLiveTrue(Pageable pageable);
	
	
	//  find product of given category
	
	Page<Product>  findByCategory(Category category,Pageable pageable);
	
}
