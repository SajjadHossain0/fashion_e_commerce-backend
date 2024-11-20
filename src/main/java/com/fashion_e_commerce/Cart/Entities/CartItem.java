package com.fashion_e_commerce.Cart.Entities;

import com.fashion_e_commerce.Order.Entities.Order;
import com.fashion_e_commerce.ProductDetails.Entities.Product;
import com.fashion_e_commerce.User.Entities.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Product product;

    private int quantity;
    private String size;
    private double totalprice;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = true)
    private Order order;

}
