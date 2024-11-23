package com.fashion_e_commerce.Order.Entities;

import com.fashion_e_commerce.Cart.Entities.CartItem;
import com.fashion_e_commerce.User.Entities.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // The user who placed the order

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<CartItem> items; // List of items in the order

    @JsonManagedReference
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<OrderItem> orderItems = new ArrayList<>();


    private Double shippingCharge;

    private double totalPrice; // Total price of the order

    private String contactInfo; // Contact information of the user

    private String shippingAddress; // Shipping address

    private String paymentMethod; // Payment method (e.g., COD)

    private String status = "Pending"; // Order status (e.g., Pending, Shipped, Delivered)

    private LocalDateTime orderDate; // Date and time the order was placed
}
