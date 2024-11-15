package com.fashion_e_commerce.Order.Controllers;

import com.fashion_e_commerce.Cart.Entities.CartItem;
import com.fashion_e_commerce.Order.Entities.Order;
import com.fashion_e_commerce.Order.Services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/place")
    public ResponseEntity<Order> placeOrder(
            @RequestParam Long userId,
            @RequestParam String contactInfo,
            @RequestParam String shippingAddress,
            @RequestParam String paymentMethod,
            @RequestParam boolean isDhaka) {
        Order order = orderService.placeOrder(userId, contactInfo, shippingAddress, paymentMethod, isDhaka);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.getOrdersByUser(userId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }
}

