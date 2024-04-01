package com.lcwd.electronic.store.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.models.User;


public interface UserService {

	
	//create user
	UserDto createUser(UserDto userDto);
	
	//  update
	UserDto updateUser(UserDto userDto,String userId);
	
	
	// delete
	 void deleteUser(String userId);
	 
	 // getall user
	 
      PageableResponse<UserDto> getAllUser(int pageNumber,int pageSize,String sortBy,String sortDir);
	 
	 
	 //get single user by id
	 
	 UserDto getUserById(String userId);
	 
	 
	 // get user by email
	 
	 UserDto getUserByEmail(String email);
	 
	 // serach user 
	 
	 List<UserDto> searchUser(String keyword);
	 
	 
	 
	 Optional<User>  findUserByEmailOptional(String email);
	 
	 
	 
	 

}
