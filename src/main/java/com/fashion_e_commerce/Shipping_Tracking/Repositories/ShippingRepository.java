package com.fashion_e_commerce.Shipping_Tracking.Repositories;

import com.fashion_e_commerce.Shipping_Tracking.Entities.Shipping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShippingRepository extends JpaRepository<Shipping, Long> {

    Shipping findByOrderId(Long orderId);  // Find shipping info by order ID

}