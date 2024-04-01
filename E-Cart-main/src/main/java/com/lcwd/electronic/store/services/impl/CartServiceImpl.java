package com.lcwd.electronic.store.services.impl;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lcwd.electronic.store.dtos.AddItemToCAartRequest;
import com.lcwd.electronic.store.dtos.CartDto;
import com.lcwd.electronic.store.exception.ImageBadApiRequest;
import com.lcwd.electronic.store.exception.ResourceNotFoundException;
import com.lcwd.electronic.store.models.Cart;
import com.lcwd.electronic.store.models.CartItem;
import com.lcwd.electronic.store.models.Product;
import com.lcwd.electronic.store.models.User;
import com.lcwd.electronic.store.repositories.CartItemRepository;
import com.lcwd.electronic.store.repositories.CartReposiory;
import com.lcwd.electronic.store.repositories.ProductRepository;
import com.lcwd.electronic.store.repositories.UserRepository;
import com.lcwd.electronic.store.services.CartService;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class CartServiceImpl implements CartService {
	
	@Autowired
	private CartReposiory cartRepository;
	
	@Autowired
	private  ProductRepository productRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private CartItemRepository cartItemRepository;
	

	@Override
	public CartDto addItemToCart(String userId, AddItemToCAartRequest request) {
		  int quantity = request.getQuantity();
	        String productId = request.getProductId();

	        if (quantity <= 0) {
	            throw new ImageBadApiRequest("Requested quantity is not valid !!");
	        }

	        //fetch the product
	        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found in database !!"));
	        //fetch the user from db
	        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found in database!!"));

	        Cart cart = null;
	        try {
	            cart = cartRepository.findByUser(user).get();
	        } catch (NoSuchElementException e) {
	            cart = new Cart();
	            cart.setCartId(UUID.randomUUID().toString());
	            cart.setCreatedAt(new Date());
	        }

	        //perform cart operations
	        //if cart items already present; then update
	        AtomicReference<Boolean> updated = new AtomicReference<>(false);
	        List<CartItem> items = cart.getItems();
	        items = items.stream().map(item -> {

	            if (item.getProduct().getProductId().equals(productId)) {
	                //item already present in cart
	                item.setQuantity(quantity);
	                item.setTotalPrice(quantity * product.getDiscount());
	                updated.set(true);
	            }
	            return item;
	        }).collect(Collectors.toList());

//	        cart.setItems(updatedItems);

	        //create items
	        if (!updated.get()) {
	            CartItem cartItem = CartItem.builder()
	                    .quantity(quantity)
	                    .totalPrice(quantity * product.getDiscount())
	                    .cart(cart)
	                    .product(product)
	                    .build();
	            cart.getItems().add(cartItem);
	        }


	        cart.setUser(user);
	        Cart updatedCart = cartRepository.save(cart);
	        return modelMapper.map(updatedCart, CartDto.class);

	}

	@Override
	public void removeItemFromCart(String userId, int cartItem) {
		   //conditions

        CartItem cartItem1 = cartItemRepository.findById(cartItem).orElseThrow(() -> new ResourceNotFoundException("Cart Item not found !!"));
        cartItemRepository.delete(cartItem1);
	}

	@Override
	public void clearCart(String userId) {
		 User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found in database!!"));
	        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart of given user not found !!"));
	        cart.getItems().clear();
	        cartRepository.save(cart);
	}
	
	  @Override
	    public CartDto getCartByUser(String userId) {
		    User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found in database!!"));
	        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart of given user not found !!"));
	        return modelMapper.map(cart, CartDto.class);
	    }
	
	

}
