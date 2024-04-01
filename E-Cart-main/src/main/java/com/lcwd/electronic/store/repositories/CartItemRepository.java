package com.lcwd.electronic.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lcwd.electronic.store.models.CartItem;

public interface CartItemRepository  extends JpaRepository<CartItem, Integer> {

}
