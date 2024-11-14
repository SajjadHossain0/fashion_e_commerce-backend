package com.fashion_e_commerce.ProductCategory.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SubCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Lob
    private byte[] image; // Stores the image as a byte array

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonIgnore // This will prevent infinite recursion
    private Category category;
}
