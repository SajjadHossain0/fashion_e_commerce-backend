package com.fashion_e_commerce.ProductCategory.Repositories;

import com.fashion_e_commerce.ProductCategory.Entities.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {
}
