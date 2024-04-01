package com.lcwd.electronic.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lcwd.electronic.store.models.Role;


public interface RoleRepository  extends JpaRepository<Role, String>{
	

}
