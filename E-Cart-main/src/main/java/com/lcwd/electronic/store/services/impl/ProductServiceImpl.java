package com.lcwd.electronic.store.services.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.ProductDto;
import com.lcwd.electronic.store.exception.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.Helper;
import com.lcwd.electronic.store.models.Category;
import com.lcwd.electronic.store.models.Product;
import com.lcwd.electronic.store.repositories.CategoryRepository;
import com.lcwd.electronic.store.repositories.ProductRepository;
import com.lcwd.electronic.store.services.CategoryService;
import com.lcwd.electronic.store.services.ProductService;


@Service
public class ProductServiceImpl  implements ProductService{
	
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	@Value("${product.image.path}")
	private String imagePath;
	
	
	@Autowired
	private CategoryRepository categoryRepository;
	

	@Override
	public ProductDto createProduct(ProductDto productDto) {
		// TODO Auto-generated method stub
		
		String productIdString = UUID.randomUUID().toString();
		productDto.setProductId(productIdString);
		
		Date date = new Date();
		
		productDto.setAddDate(date);
		
		Product product = modelMapper.map(productDto, Product.class);
		Product savedProduct= productRepository.save(product);	 
		
		return modelMapper.map(savedProduct, ProductDto.class);
	}

	@Override
	public ProductDto updateProduct(ProductDto productDto, String productId) {
		// TODO Auto-generated method stub
		
		Product product = productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("product with given id does not exist"));
		 
		
		product.setBrandName(productDto.getBrandName());
		product.setColor(productDto.getColor());
		product.setDescription(productDto.getDescription());
		product.setDiscount(productDto.getDiscount());
		product.setLive(productDto.isLive());
		product.setPrice(productDto.getPrice());
		product.setProductImage(productDto.getProductImage());
		product.setQuantity(productDto.getQuantity());
		product.setStock(product.isStock());
		product.setTitle(productDto.getTitle());
		
		 Product savedproduct = productRepository.save(product);
		 
		 
		
		
		return modelMapper.map(savedproduct, ProductDto.class);
		
	}

	@Override
	public PageableResponse<ProductDto> getAllProduct(int pageNumber,int pageSize,String sortBy,String sortDir) {
		// TODO Auto-generated method stub
		Sort sort = (sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):Sort.by(sortBy).ascending();
		Pageable  pageable = (Pageable) PageRequest.of(pageNumber, pageSize,sort);
		
		
		Page<Product> page = productRepository.findAll(pageable);
		
		PageableResponse<ProductDto> response = Helper.getPageableResponse(page, ProductDto.class);
		
		
		
		return response;
	}

	@Override
	public void deleteProduct(String productId) {
		// TODO Auto-generated method stub
		Product product = productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("product with given id does not exist"));
        
		//  delete product
	//  delete user profrfile image  from folder
		
				// images/users.abc.png
				String fullpath = imagePath + product.getProductImage();
				
				
					
					Path path = Paths.get(fullpath);
					try {
						Files.delete(path);
					}
					catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
		
		productRepository.delete(product);
	}

	@Override
	public ProductDto getProductById(String productId) {
		// TODO Auto-generated method stub
		Product product = productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("product with given id does not exist"));
   
		return modelMapper.map(product, ProductDto.class);
	}

	@Override
	public PageableResponse<ProductDto>  getByTitle(String subTitle,int pageNumber,int pageSize,String sortBy,String sortDir) {
		// TODO Auto-generated method stub
		
		Sort sort = (sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):Sort.by(sortBy).ascending();
		Pageable  pageable = (Pageable) PageRequest.of(pageNumber, pageSize,sort);
		
		Page<Product> page = productRepository.findByTitleContaining(subTitle,pageable);
		
		PageableResponse<ProductDto> response = Helper.getPageableResponse(page, ProductDto.class);
		
		
		
		return response;
	}

	

	@Override
	public PageableResponse<ProductDto>  getAllLive(int pageNumber,int pageSize,String sortBy,String sortDir) {
		// TODO Auto-generated method stub
		
		
		Sort sort = (sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):Sort.by(sortBy).ascending();
		Pageable  pageable = (Pageable) PageRequest.of(pageNumber, pageSize,sort);
		
		Page<Product> page = productRepository.findByLiveTrue(pageable);
		
		PageableResponse<ProductDto> response = Helper.getPageableResponse(page, ProductDto.class);
		
		
		
		return response;
	}

	@Override
	public ProductDto createProductWithCategory(ProductDto productDto, String categoryId) {
		// TODO Auto-generated method stub
		
		Category category = categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("category with given id is not found"));
         
		CategoryDto categoryDto = modelMapper.map(category, CategoryDto.class);
		
		String productIdString = UUID.randomUUID().toString();
		productDto.setProductId(productIdString);
		
		Date date = new Date();
		
		productDto.setAddDate(date);
		productDto.setCategory(categoryDto);
		
		Product product = modelMapper.map(productDto, Product.class);
		Product savedProduct= productRepository.save(product);	 
		
		return modelMapper.map(savedProduct, ProductDto.class);
		
		
		
	}

	@Override
	public ProductDto assignProduct(String productId, String categoryId) {
		// TODO Auto-generated method stub
		Category category = categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("category with given id is not found"));
		Product product = productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("product with given id does not exist"));
         
		
		product.setCategory(category);
		
		Product savedproduct = productRepository.save(product);
		
		
		
		
		
		return  modelMapper.map(savedproduct, ProductDto.class);
	}

	@Override
	public PageableResponse<ProductDto> getAllProductofCategory(String categoryId,int pageNumber,int pageSize,String sortBy,String sortDir) {
		// TODO Auto-generated method stub
		
		Category category = categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("category with given id is not found"));
         
		Sort sort = (sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):Sort.by(sortBy).ascending();
		Pageable  pageable = (Pageable) PageRequest.of(pageNumber, pageSize,sort);
		
		Page<Product> page = productRepository.findByCategory(category, pageable);
		
		
		PageableResponse<ProductDto> response = Helper.getPageableResponse(page, ProductDto.class);
		
		
		return response;
	}

}
