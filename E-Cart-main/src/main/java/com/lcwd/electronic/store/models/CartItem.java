package com.lcwd.electronic.store.models;

import java.util.Date;
import java.util.List;

import com.lcwd.electronic.store.dtos.CartDto;
import com.lcwd.electronic.store.dtos.CartItemDto;
import com.lcwd.electronic.store.dtos.UserDto;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "cart_items")
public class CartItem {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cartItemId;
    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;
    private  int quantity;
    private  int totalPrice;
//    mapping cart
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private  Cart cart;



}
