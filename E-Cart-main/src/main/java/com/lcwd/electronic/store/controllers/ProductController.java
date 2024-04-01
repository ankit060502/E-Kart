package com.lcwd.electronic.store.controllers;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
import com.lcwd.electronic.store.services.FileService;
import com.lcwd.electronic.store.services.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

@Tag(
        name = "CRUD REST APIs for Product Resource"
        
)
@RestController
@RequestMapping("/product")
public class ProductController {

	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private FileService fileService;
	
	@Value("${product.image.path}")
	private String imagePath;
	
	
	
	
	// create 
	@Operation(
            summary = "Create Product REST API",
            description = "Create product REST API is used to create new  product "
    )
  
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping
	public ResponseEntity<ProductDto> create(@RequestBody ProductDto productDto){
		
	ProductDto productDto2=	productService.createProduct(productDto);
	
	return new ResponseEntity<ProductDto>(productDto2,HttpStatus.CREATED);
	
	}
	
	// update
	@Operation(
            summary = "Update product REST API",
            description = "Update product REST API is  used to update the product "
    )
  
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping("/{productId}")
	public ResponseEntity<ProductDto> update(@RequestBody ProductDto productDto,@PathVariable String productId){
		
		ProductDto productDto2=	productService.updateProduct(productDto, productId);
		
		return new ResponseEntity<ProductDto>(productDto2,HttpStatus.OK);
		
		}
	
	// get single product by id
	@Operation(
            summary = "Get Product REST API",
            description = "Get Product REST API is used to get  product by productId "
    )
	@GetMapping("/{productId}")
	public ResponseEntity<ProductDto> getProductById(@PathVariable String productId){
		
	ProductDto productDto2=	productService.getProductById(productId);
	
	return new ResponseEntity<ProductDto>(productDto2,HttpStatus.OK);
	
	}
	
	//  delete product
	@Operation(
            summary = "Delete Product REST API",
            description = "Delete Product REST API is used to delete  product by productId "
    )
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/{productId}")
	
	public ResponseEntity<ApiResponseMessage> delete(@PathVariable String productId){
		
		productService.deleteProduct(productId);
		
		ApiResponseMessage responseMessage = ApiResponseMessage.builder()
				.message("product is deleted successfully !!")
				.status(HttpStatus.OK)
				.success(true).build();
		
		return new ResponseEntity<ApiResponseMessage>(responseMessage,HttpStatus.OK);
	}
	
	//  get all product
	@Operation(
            summary = "Get All Product REST API",
            description = "Get All Product REST API used to fetch all product "
    )
	@GetMapping
	public ResponseEntity<PageableResponse<ProductDto>> getAllProduct(
			@RequestParam(value="pageNumber",defaultValue = "0",required = false)	int pageNumber,
			@RequestParam(value="pageSize",defaultValue = "10",required = false) int pageSize,
			@RequestParam(value="sortBy",defaultValue = "title",required = false)	String sortBy,
			@RequestParam(value="sortDir",defaultValue = "asc",required = false) String sortDir
			){
		
		PageableResponse<ProductDto> response = productService.getAllProduct(pageNumber, pageSize, sortBy, sortBy);
		
		return new ResponseEntity<PageableResponse<ProductDto>>(response,HttpStatus.OK);
	}
	
	
	// get all product by given title
	@Operation(
            summary = "Get All Product REST API",
            description = "Get All Product REST API used to fetch all product with given title "
    )
	@GetMapping("/search/{subTitle}")
	public ResponseEntity<PageableResponse<ProductDto>> getAllProductByTitle(
			@PathVariable String subTitle,
			@RequestParam(value="pageNumber",defaultValue = "0",required = false)	int pageNumber,
			@RequestParam(value="pageSize",defaultValue = "10",required = false) int pageSize,
			@RequestParam(value="sortBy",defaultValue = "title",required = false)	String sortBy,
			@RequestParam(value="sortDir",defaultValue = "asc",required = false) String sortDir
			){
		
		PageableResponse<ProductDto> response = productService.getByTitle(subTitle, pageNumber, pageSize, sortBy, sortDir);
		
		return new ResponseEntity<PageableResponse<ProductDto>>(response,HttpStatus.OK);
	}
	
	// get all product which is live =true
	@Operation(
            summary = "Get All Live  Product REST API",
            description = "Get All Live Product REST API used to fetch all live  product "
    )
	@GetMapping("/live")
	public ResponseEntity<PageableResponse<ProductDto>> getAllProductByLive(
			
			@RequestParam(value="pageNumber",defaultValue = "0",required = false)	int pageNumber,
			@RequestParam(value="pageSize",defaultValue = "10",required = false) int pageSize,
			@RequestParam(value="sortBy",defaultValue = "title",required = false)	String sortBy,
			@RequestParam(value="sortDir",defaultValue = "asc",required = false) String sortDir
			){
		
		PageableResponse<ProductDto> response = productService.getAllLive( pageNumber, pageSize, sortBy, sortDir);
		
		return new ResponseEntity<PageableResponse<ProductDto>>(response,HttpStatus.OK);
	}
	
	//  upload product image
	
	@Operation(
            summary = "Upload Product Image REST API",
            description = "Upload Product Image REST API is used to upload ImageUrl of product by productId "
    )
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/image/{productId}")	
	public ResponseEntity<ImageResponse> uploadImage(@PathVariable String productId,
			@RequestParam("productImage") MultipartFile image) throws IOException{
		
		
        String imageName = fileService.uploadImage(image, imagePath);
		
		ProductDto productDto = productService.getProductById(productId);
		productDto.setProductImage(imageName);
		
		productService.updateProduct(productDto, productId);	
		
		
		ImageResponse imageResponse = ImageResponse.builder()
				.imageName(imageName).success(true).message("product image uploaded succesfully !!")
				.status(HttpStatus.CREATED).build();
		
		return new ResponseEntity<>(imageResponse,HttpStatus.CREATED);
		
	}
	
	@Operation(
            summary = "Serve Product Image REST API",
            description = "Serve Product Image REST API is used to serve ImageUrl of product by productId "
    )
	 @GetMapping("/image/{productId}")
		public void serveProductImage(@PathVariable String productId,HttpServletResponse response) throws IOException {
		
		
		
		
		ProductDto productDto = productService.getProductById(productId);
		
		
		
		InputStream resource = fileService.getResources(imagePath, productDto.getProductImage());
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		
		StreamUtils.copy(resource, response.getOutputStream());
	}
	
}
