package com.lcwd.electronic.store.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.lcwd.electronic.store.models.User;
import com.lcwd.electronic.store.repositories.UserRepository;

@Service
public class CustomUserDetailService implements UserDetailsService {

	
	@Autowired
	private UserRepository userRepository;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		User user = userRepository.findByEmail(username).orElseThrow(()->new UsernameNotFoundException("user with given id doest not exist!!"));
		
		return user;
	}

}
