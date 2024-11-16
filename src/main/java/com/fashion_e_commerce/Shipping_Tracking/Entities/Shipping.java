package com.fashion_e_commerce.Shipping_Tracking.Entities;


import com.fashion_e_commerce.Order.Entities.Order;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Shipping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Order order;  // Link to the Order entity

    private String trackingNumber;  // The shipping carrier's tracking number

    private String shippingProvider;  // Example: "Bkash", "DHL", etc.

    private String status;  // Example: "Pending", "Shipped", "In Transit", "Delivered"

    private LocalDateTime updatedAt;  // Timestamp for when the status was updated

    private LocalDateTime estimatedDelivery;  // Estimated delivery date

}