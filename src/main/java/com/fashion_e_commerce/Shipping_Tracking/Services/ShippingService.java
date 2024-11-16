package com.fashion_e_commerce.Shipping_Tracking.Services;


import com.fashion_e_commerce.Order.Entities.Order;
import com.fashion_e_commerce.Shipping_Tracking.Entities.Shipping;
import com.fashion_e_commerce.Shipping_Tracking.Repositories.ShippingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ShippingService {

    @Autowired
    private ShippingRepository shippingRepository;

    public Shipping createShipping(Shipping shipping) {
        // Save and return the shipping record
        return shippingRepository.save(shipping);
    }

    public Shipping updateShippingStatus(Long orderId, String status) {
        // Find the shipping record by orderId and update its status
        Shipping shipping = shippingRepository.findByOrderId(orderId);
        if (shipping != null) {
            shipping.setStatus(status);
            return shippingRepository.save(shipping);
        }
        return null; // Or throw an exception if shipping not found
    }

    public Shipping getShippingByOrderId(Long orderId) {
        return shippingRepository.findByOrderId(orderId);
    }
}