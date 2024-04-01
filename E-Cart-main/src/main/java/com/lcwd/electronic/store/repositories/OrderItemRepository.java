package com.lcwd.electronic.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lcwd.electronic.store.models.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer>
{
}
