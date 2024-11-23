package com.fashion_e_commerce.Order.Repositories;

import com.fashion_e_commerce.Order.Entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}