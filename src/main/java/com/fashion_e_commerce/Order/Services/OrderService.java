package com.fashion_e_commerce.Order.Services;

import com.fashion_e_commerce.Cart.Entities.CartItem;
import com.fashion_e_commerce.Cart.Services.CartService;
import com.fashion_e_commerce.Order.Entities.Order;
import com.fashion_e_commerce.Order.Entities.OrderItem;
import com.fashion_e_commerce.Order.Repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartService cartService; // Assuming a cart service exists

    public Order placeOrder(Long userId, String contactInfo, String shippingAddress, String paymentMethod) {
        // Fetch cart items for the user
        List<CartItem> cartItems = cartService.getCartItems(userId);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty. Cannot place order.");
        }

        // Calculate total price
        double totalPrice = cartItems.stream()
                .mapToDouble(CartItem::getTotalprice)
                .sum();

        // Create an order
        Order order = new Order();
        order.setUser(cartItems.get(0).getUser()); // Assuming all cart items belong to the same user
        order.setItems(cartItems);
        order.setTotalPrice(totalPrice);
        order.setContactInfo(contactInfo);
        order.setShippingAddress(shippingAddress);
        order.setPaymentMethod(paymentMethod);
        order.setOrderDate(LocalDateTime.now());

        // Save the order
        Order savedOrder = orderRepository.save(order);

        // Clear the user's cart
        cartService.clearCart(userId);

        return savedOrder;
    }

    public List<Order> getOrdersByUser(Long userId) {
        return orderRepository.findByUser_Id(userId);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll(); // Admin feature to view all orders
    }
}
