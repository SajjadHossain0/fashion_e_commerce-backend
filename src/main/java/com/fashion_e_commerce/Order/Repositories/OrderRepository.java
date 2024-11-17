package com.fashion_e_commerce.Order.Repositories;

import com.fashion_e_commerce.Order.Entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser_Id(Long userId); // Retrieve orders for a specific user
}
