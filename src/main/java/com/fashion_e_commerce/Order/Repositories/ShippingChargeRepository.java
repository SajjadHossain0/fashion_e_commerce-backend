package com.fashion_e_commerce.Order.Repositories;


import com.fashion_e_commerce.Order.Entities.ShippingCharge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShippingChargeRepository extends JpaRepository<ShippingCharge, Long> {
}
