package com.lcwd.electronic.store.controllers;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lcwd.electronic.store.dtos.ApiResponseMessage;
import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.dtos.ImageResponse;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.ProductDto;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.services.CategoryService;
import com.lcwd.electronic.store.services.FileService;
import com.lcwd.electronic.store.services.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;


@Tag(
        name = "CRUD REST APIs for Product Category "
        
)
@RestController
@RequestMapping("/categories")
public class CategoryController {
	
	
	@Autowired
	private CategoryService categoryService;
	
	@Value("${category.cover.image.path}")
	private String imageUploadPath;
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private ProductService productService;
	
	
	//create category 
	@Operation(
            summary = "Create Category REST API",
            description = "Create Category REST API is used to create new  product's Category"
    )
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping
	public ResponseEntity<CategoryDto> create( @Valid @RequestBody CategoryDto categoryDto){
		
		CategoryDto  categoryDto2 = categoryService.create(categoryDto);
		
		return new ResponseEntity<>(categoryDto2,HttpStatus.CREATED);
		
		
	}
	
	// update  category 
	@Operation(
            summary = "Update Category REST API",
            description = "Update Category REST API is used to Update   product's Category with categoryId"
    )
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping("/{categoryId}")
	public ResponseEntity<CategoryDto>  updaECategory(@Valid @RequestBody CategoryDto categoryDto ,@PathVariable String categoryId){
		
		CategoryDto categoryDto2 = categoryService.update(categoryDto, categoryId);
		
		return new ResponseEntity<>(categoryDto2,HttpStatus.OK);
		
		
	}
	
	
	
	
	// get all category 
	@Operation(
            summary = "Get  Category REST API",
            description = "Get Category REST API is used to get all   Category  "
    )
	@GetMapping
	public ResponseEntity<PageableResponse<CategoryDto>> getAllCategory(@RequestParam(value="pageNumber", required = false,defaultValue = "0") int pageNumber,
			@RequestParam(value="pageSize", required = false,defaultValue = "10") int pageSize,
			@RequestParam(value="sortBy",defaultValue = "title",required = false)	String sortBy,
			@RequestParam(value="sortDir",defaultValue = "asc",required = false) String sortDir
	
			){
		
		PageableResponse<CategoryDto> response = categoryService.getAll(pageNumber, pageSize, sortBy, sortDir);
		
		return new ResponseEntity<>(response,HttpStatus.OK);
		
		
		
	}
	
	// delete  category
	@Operation(
            summary = "Delete  Category REST API",
            description = "Delete Category REST API is used to Delete    Category with categoryId "
    )
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/{categoryId}")
	public ResponseEntity<ApiResponseMessage> deleteCategory(@PathVariable String categoryId){
		
		
		categoryService.delete(categoryId);
		
		ApiResponseMessage responseMessage = ApiResponseMessage.builder()
				.message("Ctaegory deleted successfully!!")
				.status(HttpStatus.OK).success(true).build();
		
		return new ResponseEntity<>(responseMessage,HttpStatus.OK);
		
	}
	
	
	// get single category
	
	@Operation(
            summary = "Get  Category REST API",
            description = "Get Category REST API is used to get Category with categoryId  "
    )
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	
	@GetMapping("/{categoryId}")
	public ResponseEntity<CategoryDto> getCategoryById(@PathVariable String categoryId){
		
		CategoryDto categoryDto = categoryService.getCategoryById(categoryId);
		
		return new ResponseEntity<>(categoryDto,HttpStatus.OK);
		
	}
	
//  upload category image
	@Operation(
            summary = "UploadImage  Category REST API",
            description = "UploadImage REST API is used to upload  CategoryImage url with categoryId  "
    )
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/image/{categoryId}")
	public ResponseEntity<ImageResponse> uploadCategoryImage(@RequestParam ("coverImage") MultipartFile image,@PathVariable String categoryId) throws IOException{
		
		
		String imageName = fileService.uploadImage(image, imageUploadPath);
		
		CategoryDto  categoryDto = categoryService.getCategoryById(categoryId);
		categoryDto.setCoverImage(imageName);
		
		categoryService.update(categoryDto, categoryId);
		
		
		
		
		
		ImageResponse imageResponse = ImageResponse.builder()
				.imageName(imageName).success(true)
				.status(HttpStatus.CREATED).build();
		
		return new ResponseEntity<>(imageResponse,HttpStatus.CREATED);
		
		
	}
	
	@Operation(
            summary = "servreImage  Category REST API",
            description = "servreImage REST API is used to upload  servreImage url with categoryId  "
    )
	
	 @GetMapping("/image/{categoryId}")
		public void serveCategoryImage(@PathVariable String categoryId,HttpServletResponse response) throws IOException {
		
		
		
		
		CategoryDto categoryDto = categoryService.getCategoryById(categoryId);
		
		
		InputStream resource = fileService.getResources(imageUploadPath, categoryDto.getCoverImage());
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		
		StreamUtils.copy(resource, response.getOutputStream());
	}
	 
	 
	 // create product with category
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	 @PostMapping("/{categoryId}/product")
	 
	 public ResponseEntity<ProductDto> createProductWithCategory(@PathVariable String categoryId,
			 @RequestBody ProductDto productDto	 ){
		 
		 
		 ProductDto productDto2 = productService.createProductWithCategory(productDto, categoryId);
		 
		 return new ResponseEntity<ProductDto>(productDto2,HttpStatus.CREATED);
		 
	 }
	 
	 // assigning product of given id with category of given id
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	 @PutMapping("/{categoryId}/product/{productId}")
	 
	 public ResponseEntity<ProductDto> assignProductWithCategory(@PathVariable String categoryId,
			@PathVariable String productId ){
		 
		 
		 ProductDto productDto2 = productService.assignProduct(productId, categoryId);
		 
		 return new ResponseEntity<ProductDto>(productDto2,HttpStatus.CREATED);
		 
	 }
	 
	   @GetMapping("/{categoryId}/product")
		public ResponseEntity<PageableResponse<ProductDto>> getAllProductofCategory(@RequestParam(value="pageNumber", required = false,defaultValue = "0") int pageNumber,
				@RequestParam(value="pageSize", required = false,defaultValue = "10") int pageSize,
				@RequestParam(value="sortBy",defaultValue = "title",required = false)	String sortBy,
				@RequestParam(value="sortDir",defaultValue = "asc",required = false) String sortDir,
		         @PathVariable String categoryId
				){
			
			PageableResponse<ProductDto> response = productService.getAllProductofCategory(categoryId, pageNumber, pageSize, sortBy, sortDir);
			
			
			return new ResponseEntity<>(response,HttpStatus.OK);
			
			
			
		}
	 
	 
	 
	    
	    
	
	

}
