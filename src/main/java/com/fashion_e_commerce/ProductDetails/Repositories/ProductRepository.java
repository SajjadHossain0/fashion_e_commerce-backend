package com.fashion_e_commerce.ProductDetails.Repositories;

import com.fashion_e_commerce.ProductDetails.Entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Find products by category
    List<Product> findByCategory_Id(Long categoryId);

    // Find products by subcategory
    List<Product> findBySubCategory_Id(Long subCategoryId);

    @Query("SELECT p FROM Product p " +
            "WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.detaileddescription) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.brand) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR :keyword MEMBER OF p.tags")
    List<Product> searchProducts(@Param("keyword") String keyword);

    List<Product> findByCategoryId(Long categoryId);
    List<Product> findBySubCategoryId(Long subCategoryId);

}
