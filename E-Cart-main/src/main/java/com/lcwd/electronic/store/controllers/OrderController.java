package com.lcwd.electronic.store.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.lcwd.electronic.store.dtos.ApiResponseMessage;
import com.lcwd.electronic.store.dtos.CreateOrderRequest;
import com.lcwd.electronic.store.dtos.OrderDto;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.services.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import java.util.List;

@Tag(
        name = "CRUD REST APIs for Order "
        
)
@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    //create
    @Operation(
            summary = "Create Order REST API",
            description = "Create Order REST API is used to create new  order "
    )
  
    @PreAuthorize("hasRole('ROLE_NORMAL')")
    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        OrderDto order = orderService.createOrder(request);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Remove Order REST API",
            description = "Remove Order REST API is used to remove order with OrderId "
    )
    
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponseMessage> removeOrder(@PathVariable String orderId) {
        orderService.removeOrder(orderId);
        ApiResponseMessage responseMessage = ApiResponseMessage.builder()
                .status(HttpStatus.OK)
                .message("order is removed !!")
                .success(true)
                .build();
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);

    }

    //get orders of the user
    @Operation(
            summary = "Get Order REST API",
            description = "Get Order REST API is used to get all order of  a User with given UserId "
    )
    @GetMapping("/users/{userId}")
    public ResponseEntity<List<OrderDto>> getOrdersOfUser(@PathVariable String userId) {
        List<OrderDto> ordersOfUser = orderService.getOrdersOfUser(userId);
        return new ResponseEntity<>(ordersOfUser, HttpStatus.OK);
    }
    @Operation(
            summary = "Get Order REST API",
            description = "Get Order REST API is used to get all order  "
    )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<PageableResponse<OrderDto>> getOrders(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "orderedDate", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "desc", required = false) String sortDir
    ) {
        PageableResponse<OrderDto> orders = orderService.getOrders(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }


}
