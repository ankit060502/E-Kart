package com.lcwd.electronic.store.services.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import com.lcwd.electronic.store.exception.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.Helper;
import com.lcwd.electronic.store.models.Category;
import com.lcwd.electronic.store.repositories.CategoryRepository;
import com.lcwd.electronic.store.services.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Value("${category.cover.image.path}")
	private String imageUploadPath;
	@Override
	public CategoryDto create(CategoryDto categoryDto) {
		// TODO Auto-generated method stub
		
		String categoryId = UUID.randomUUID().toString();
		categoryDto.setCategoryId(categoryId);
	
		
		Category category = modelMapper.map(categoryDto, Category.class);
		Category savedCategory = categoryRepository.save(category);
		
		
		
		
		
		return modelMapper.map(savedCategory, CategoryDto.class);
	}

	@Override
	public CategoryDto update(CategoryDto categoryDto, String categoryId) {
		// TODO Auto-generated method stub
		
		Category category = categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("category with given id is not found"));
		
		category.setDescription(categoryDto.getDescription());
		category.setCoverImage(categoryDto.getCoverImage());
		category.setTitle(categoryDto.getTitle());
		
		Category updatedCategory = categoryRepository.save(category);
		
		
		return modelMapper.map(updatedCategory, CategoryDto.class);
	}

	@Override
	public void delete(String categoryId) {
		// TODO Auto-generated method stub
		Category category = categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("category with given id is not found"));
		
	//  delete user profrfile image  from folder
		
			// images/users.abc.png
			String fullpath = imageUploadPath + category.getCoverImage();
			
			
				
				Path path = Paths.get(fullpath);
				try {
					Files.delete(path);
				}
				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
		
		categoryRepository.delete(category);
		
	}

	@Override
	public PageableResponse<CategoryDto> getAll(int pageNumber ,int pageSize,String sortBy,String sortDir) {
		// TODO Auto-generated method stub
		
		Sort sort = (sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):Sort.by(sortBy).ascending();
		Pageable  pageable = (Pageable) PageRequest.of(pageNumber, pageSize,sort);
		
		Page<Category> page = categoryRepository.findAll(pageable);
		
		PageableResponse<CategoryDto> response = Helper.getPageableResponse(page, CategoryDto.class);
		
		
		return response;
	}

	@Override
	public CategoryDto getCategoryById(String categoryId) {
		// TODO Auto-generated method stub
		
	 Category category = categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("category with given id is not found"));
	
	   
		return modelMapper.map(category, CategoryDto.class);
	}

}
