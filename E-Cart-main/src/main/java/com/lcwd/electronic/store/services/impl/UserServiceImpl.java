package com.lcwd.electronic.store.services.impl;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.exception.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.Helper;
import com.lcwd.electronic.store.models.Role;
import com.lcwd.electronic.store.models.User;
import com.lcwd.electronic.store.repositories.RoleRepository;
import com.lcwd.electronic.store.repositories.UserRepository;
import com.lcwd.electronic.store.services.UserService;

@Service
public class UserServiceImpl  implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Value("${user.profile.image.path}")
	private String imageUploadPath;
	
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Value("${normal.role.id}")
	private String normal_role_id;
	
	
	@Autowired
	private RoleRepository roleRepository;
	

	@Override
	public UserDto createUser(UserDto userDto) {
	  
		// generate unique id in string format
		String userId = UUID.randomUUID().toString();
		userDto.setUserId(userId);
		userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
		
		//  dto-> entity
		
		
		
		User user = dtoToUser(userDto);
		
		
		// fetch role  of normal and set it to user
		Role role = roleRepository.findById(normal_role_id).get();
		user.getRoles().add(role);
		
		User saveUser =  userRepository.save(user);
		
		// entity-> dto
		
		UserDto newUserDto = userToDto(saveUser);
		
		
		
		return newUserDto;
	}

	@Override
	public UserDto updateUser(UserDto userDto, String userId) {
		// TODO Auto-generated method stub
		
		User user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("user not found with given id"));
		
		user.setAbout(userDto.getAbout());
		user.setName(userDto.getName());
		user.setPassword(userDto.getPassword());
		user.setUserImage(userDto.getUserImage());
		user.setGender(userDto.getGender());
		
		User updatedUser = userRepository.save(user);
		UserDto updatedDto = userToDto(updatedUser);
		
		return updatedDto;
	}

	@Override
	public void deleteUser(String userId) {
		// TODO Auto-generated method stub
		User user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("user not found with given id"));
		
		
		//  delete user profrfile image  from folder
		
		// images/users.abc.png
		String fullpath = imageUploadPath + user.getUserImage();
		
		
			
			Path path = Paths.get(fullpath);
			try {
				Files.delete(path);
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		
		
		
		userRepository.delete(user);
		
		
	}

	@Override
	public PageableResponse<UserDto> getAllUser(int pageNumber,int pageSize,String sortBy,String sortDir) {
		// TODO Auto-generated method stub
		
		Sort sort =(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):Sort.by(sortBy).ascending();
		
		
		// PAGE NUMBER dafault start from 0
		Pageable  pageable = (Pageable) PageRequest.of(pageNumber, pageSize,sort);
		Page<User> page = userRepository.findAll(pageable);
		
		
		
		PageableResponse<UserDto> pageableResponse = Helper.getPageableResponse(page, UserDto.class);
		
		
		
		return pageableResponse;
	}

	@Override
	public UserDto getUserById(String userId) {
		// TODO Auto-generated method stub
		User user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("user not found with given id"));

		return userToDto(user);
	}

	@Override
	public UserDto getUserByEmail(String email) {
		// TODO Auto-generated method stub
		User user  = userRepository.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("user not found with given email") );
		return userToDto(user);
	}

	@Override
	public List<UserDto> searchUser(String keyword) {
		// TODO Auto-generated method stub
		
		List<User> users= userRepository.findByNameContaining(keyword);
		List<UserDto> dtolist = users.stream().map(user-> userToDto(user)).collect(Collectors.toList());
		
		return dtolist;
	}
	
	private UserDto userToDto(User user) {
		/*
		UserDto userDto = UserDto.builder()
				.userId(user.getUserId())
				.name(user.getName())
				.email(user.getEmail())
				.password(user.getPassword())
				.gender(user.getGender())
				.about(user.getAbout())
				.userImage(user.getUserImage()).build();
		
		return userDto;
		*/
		return modelMapper.map(user, UserDto.class);
	}
	
	
	
	private User dtoToUser(UserDto userDto) {
		/*
		User user = User.builder()
				.userId(userDto.getUserId())
				.name(userDto.getName())
				.email(userDto.getEmail())
				.password(userDto.getPassword())
				.about(userDto.getAbout())
				.gender(userDto.getGender())
				.userImage(userDto.getUserImage()).build();
		
		return user;
		*/
		return modelMapper.map(userDto, User.class);
		
	}

	@Override
	public Optional<User> findUserByEmailOptional(String email) {
		// TODO Auto-generated method stub
		
		
		return userRepository.findByEmail(email);
	}
	

}
