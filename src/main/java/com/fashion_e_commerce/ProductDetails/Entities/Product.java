package com.fashion_e_commerce.ProductDetails.Entities;

import com.fashion_e_commerce.ProductCategory.Entities.Category;
import com.fashion_e_commerce.ProductCategory.Entities.SubCategory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private double price;
    private double discount;
    private String description;
    private String detaileddescription;
    private String brand;
    @ManyToOne
    private Category category;
    @ManyToOne
    private SubCategory subCategory;

    @ElementCollection
    private List<String> sizes;//available size - M,L,XL,2XL,free-size

    private String material;//e.g., Cotton, Polyester
    private int stock;

    private boolean available;

    @ElementCollection
    private List<String> tags;

    public double getDiscountedPrice() {
        BigDecimal discountedPrice = new BigDecimal(price - (price * (discount / 100)));
        discountedPrice = discountedPrice.setScale(2, RoundingMode.HALF_UP);  // Rounds to 2 decimal places
        return discountedPrice.doubleValue();
    }

    // Constructor for setting only the ID
    public Product(Long id) {
        this.id = id;
    }


    @Lob
    private byte[] image;

}
