package com.fashion_e_commerce.ProductDetails.Entities;

import com.fashion_e_commerce.ProductCategory.Entities.Category;
import com.fashion_e_commerce.ProductCategory.Entities.SubCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Lob
    private byte[] image;

    private boolean available;

    public double getDiscountedPrice() {
        return price - (price * (discount / 100));
    }

}
