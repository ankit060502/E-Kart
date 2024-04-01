package com.lcwd.electronic.store.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

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
import com.lcwd.electronic.store.dtos.ImageResponse;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.services.FileService;
import com.lcwd.electronic.store.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;


@Tag(
        name = "CRUD REST APIs for User Resource"
        
)
@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private FileService fileService;
	
	@Value("${user.profile.image.path}")
	private String imageUploadPath;
	
	
	// create user
	 @Operation(
	            summary = "Create User REST API",
	            description = "Create User REST API is used to create new  user "
	    )
	  
	
	@PostMapping
	public ResponseEntity<UserDto> createUser( @Valid @RequestBody UserDto userDto){
		
		UserDto userDto1 = userService.createUser(userDto);
		
		return new ResponseEntity<>(userDto1,HttpStatus.CREATED);
	}
	
	// update user
	 
	 @Operation(
	            summary = "Update User REST API",
	            description = "Update User REST API is used to update user by Id"
	    )
	 
	
	@PutMapping("/{userId}")
	public ResponseEntity<UserDto> updateUser(@PathVariable("userId") String userId, @Valid @RequestBody UserDto userDto ){
		
		UserDto userDto1  = userService.updateUser(userDto,userId);
		return new ResponseEntity<>(userDto1,HttpStatus.OK);
		
		
	}  
	// delete
	 
	 @Operation(
	            summary = "Delete User REST API",
	            description = "Delete User REST API is used to delete user by Id"
	    )
	 
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/{userId}")
	public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable String userId){
		
		userService.deleteUser(userId);
		
		ApiResponseMessage message = ApiResponseMessage.builder()
				.message("user is deleted succesfully!!")
				.status(HttpStatus.OK)
				.success(true).build();
		
		return new ResponseEntity<>(message ,HttpStatus.OK);
	}
	
	// getall  users
	 @Operation(
	            summary = "GET All User REST API",
	            description = "GET All User REST API is used to get all users"
	    )
	 
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping
	public ResponseEntity<PageableResponse<UserDto>> getAllUsers(
		@RequestParam(value="pageNumber",defaultValue = "0",required = false)	int pageNumber,
		@RequestParam(value="pageSize",defaultValue = "10",required = false) int pageSize,
		@RequestParam(value="sortBy",defaultValue = "name",required = false)	String sortBy,
		@RequestParam(value="sortDir",defaultValue = "asc",required = false) String sortDir
		
			){
		
		PageableResponse<UserDto> response =  userService.getAllUser(pageNumber,pageSize,sortBy,sortDir);
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
	// get single 
	 @Operation(
	            summary = "Get User REST API",
	            description = "Get User REST API is used to get user by userId"
	    )
	
	@PreAuthorize("hasRole('ROLE_NORMAL')")
	@GetMapping("/{userId}")
	public ResponseEntity<UserDto> getUserById(@PathVariable String userId){
		
		UserDto userdto = userService.getUserById(userId);
		
		return new ResponseEntity<>(userdto, HttpStatus.OK);
	}
	
	
	
	// getby email
	 @Operation(
	            summary = "Get User REST API",
	            description = "Get User REST API is used to get user by userEmail"
	    )
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/email/{email}")
	public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email){
		
		UserDto userdto = userService.getUserByEmail(email);
		
		
		return new ResponseEntity<>(userdto, HttpStatus.OK);
	}
	
	
	
	
	// serach user
	 
	 @Operation(
	            summary = "Search User REST API",
	            description = "Search User REST API is used to search user by keywords"
	    )
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/search/{keywords}")
	public ResponseEntity<List<UserDto>>searchUser(@PathVariable String keywords){
		
		List<UserDto> list  = userService.searchUser(keywords);
		
		
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
	 
	//  upload user image
	 
	 @Operation(
	            summary = "Image User REST API",
	            description = "Image User REST API is used to Upload imageUrl of  user by userId"
	    )
	 
	@PostMapping("/image/{userId}")
	public ResponseEntity<ImageResponse> uploadUserImage(@RequestParam ("userImage") MultipartFile image,@PathVariable String userId) throws IOException{
		
		
		String imageName = fileService.uploadImage(image, imageUploadPath);
		
		UserDto  user = userService.getUserById(userId);
		user.setUserImage(imageName);
		
		userService.updateUser(user, userId);
		
		ImageResponse imageResponse = ImageResponse.builder()
				.imageName(imageName).success(true)
				.status(HttpStatus.CREATED).build();
		
		return new ResponseEntity<>(imageResponse,HttpStatus.CREATED);
		
		
	}
	
	 @Operation(
	            summary = "Image User REST API",
	            description = "Image User REST API is used to serve imageUrl of  user by userId"
	    )
	    @GetMapping("/image/{userId}")
		public void serveUserImage(@PathVariable String userId,HttpServletResponse response) throws IOException {
		
		
		
		UserDto user = userService.getUserById(userId);
		
		InputStream resource = fileService.getResources(imageUploadPath, user.getUserImage());
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		
		StreamUtils.copy(resource, response.getOutputStream());
	}
	    
	    
	    
	
	
	

}
