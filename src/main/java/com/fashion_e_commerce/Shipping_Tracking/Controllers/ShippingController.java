package com.fashion_e_commerce.Shipping_Tracking.Controllers;


import com.fashion_e_commerce.Order.Entities.Order;
import com.fashion_e_commerce.Order.Services.OrderService;
import com.fashion_e_commerce.Shipping_Tracking.Entities.Shipping;
import com.fashion_e_commerce.Shipping_Tracking.Services.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


@RestController
@RequestMapping("/api/shipping")
@CrossOrigin
public class ShippingController {

    @Autowired
    private ShippingService shippingService;

    @Autowired
    private OrderService orderService; // Service to retrieve the Order by ID

    @PostMapping("/create")
    public Shipping createShipping(@RequestParam Long orderId,
                                   @RequestParam String trackingNumber,
                                   @RequestParam String shippingProvider,
                                   @RequestParam String status,
                                   @RequestParam String estimatedDelivery) {

        // Retrieve the Order by orderId
        Order order = orderService.getOrderById(orderId);

        if (order == null) {
            throw new RuntimeException("Order not found");
        }

        // Create a new shipping record
        Shipping shipping = new Shipping();
        shipping.setOrder(order); // Set the order to this shipping record
        shipping.setTrackingNumber(trackingNumber);
        shipping.setShippingProvider(shippingProvider);
        shipping.setStatus(status);
        shipping.setEstimatedDelivery(LocalDateTime.parse(estimatedDelivery));

        // Save and return the shipping record
        return shippingService.createShipping(shipping);
    }

    @PutMapping("/update/{orderId}")
    public Shipping updateShippingStatus(@PathVariable Long orderId, @RequestParam String status) {
        return shippingService.updateShippingStatus(orderId, status);
    }

    @GetMapping("/status/{orderId}")
    public Shipping getShippingStatus(@PathVariable Long orderId) {
        return shippingService.getShippingByOrderId(orderId);
    }
}