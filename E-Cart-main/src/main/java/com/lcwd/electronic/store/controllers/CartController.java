package com.lcwd.electronic.store.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lcwd.electronic.store.dtos.AddItemToCAartRequest;
import com.lcwd.electronic.store.dtos.ApiResponseMessage;
import com.lcwd.electronic.store.dtos.CartDto;
import com.lcwd.electronic.store.services.CartService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/carts")
@Tag(
        name = "CRUD REST APIs for Cart  "
        
)
public class CartController {

    @Autowired
    private CartService cartService;

    
    
    //add items to cart
    @Operation(
            summary = "Add REST API",
            description = "Add REST API is used to add productItem to cart"
    )
    @PreAuthorize("hasRole('ROLE_NORMAL')")
    @PostMapping("/{userId}")
    public ResponseEntity<CartDto> addItemToCart(@PathVariable String userId, @RequestBody AddItemToCAartRequest request) {
        CartDto cartDto = cartService.addItemToCart(userId, request);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }
    
    
    
    @Operation(
            summary = "Remove REST API",
            description = "Remove REST API is used to Remove productItem from cart with userId and ItemId"
    )
    @PreAuthorize("hasRole('ROLE_NORMAL')")
    @DeleteMapping("/{userId}/items/{itemId}")
    public ResponseEntity<ApiResponseMessage> removeItemFromCart(@PathVariable String userId, @PathVariable int itemId) {
        cartService.removeItemFromCart(userId, itemId);
        ApiResponseMessage response = ApiResponseMessage.builder()
                .message("Item is removed !!")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    //clear cart
    @Operation(
            summary = "Clear REST API",
            description = "Clear REST API is used to Clear cart oF user by userId"
    )
    @PreAuthorize("hasRole('ROLE_NORMAL')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> clearCart(@PathVariable String userId) {
        cartService.clearCart(userId);
        ApiResponseMessage response = ApiResponseMessage.builder()
                .message("Now cart is blank !!")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    //get  items to cart
    @Operation(
            summary = "get REST API",
            description = "get REST API is used to get item from cart oF user by userId"
    )
    @PreAuthorize("hasRole('ROLE_NORMAL')")
    @GetMapping("/{userId}")
    public ResponseEntity<CartDto> getCart(@PathVariable String userId) {
        CartDto cartDto = cartService.getCartByUser(userId);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }

}