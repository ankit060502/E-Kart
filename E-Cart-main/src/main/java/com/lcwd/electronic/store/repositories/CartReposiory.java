package com.lcwd.electronic.store.repositories;

import java.util.Optional;

import org.hibernate.type.descriptor.converter.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;

import com.lcwd.electronic.store.models.Cart;
import com.lcwd.electronic.store.models.User;

public interface CartReposiory extends JpaRepository<Cart, String>  {
	
	
	Optional<Cart> findByUser(User user);
	

}
