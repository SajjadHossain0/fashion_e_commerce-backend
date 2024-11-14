package com.fashion_e_commerce.ProductDetails.Repositories;

import com.fashion_e_commerce.ProductDetails.Entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
