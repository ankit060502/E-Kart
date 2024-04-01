package com.lcwd.electronic.store.services;

import org.apache.coyote.http11.filters.VoidInputFilter;

import com.lcwd.electronic.store.dtos.AddItemToCAartRequest;
import com.lcwd.electronic.store.dtos.CartDto;

public interface CartService {

	
	// add item to cart;
	// case1 :  cart for user is not available : then we will create  the cart and then add item to the cart
	
	// case2 : cart availablw add the items  to cart
	
	CartDto addItemToCart (String userId,AddItemToCAartRequest request);
	
	
	// remove item from cart
	void removeItemFromCart(String userId,int cartItem);
	
	// remove all item from cart
	
	void clearCart(String userId);
	
	
	
	  CartDto getCartByUser(String userId);
}
