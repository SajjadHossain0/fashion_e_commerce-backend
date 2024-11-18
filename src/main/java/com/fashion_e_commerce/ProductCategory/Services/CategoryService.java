package com.fashion_e_commerce.ProductCategory.Services;

import com.fashion_e_commerce.ProductCategory.Entities.Category;
import com.fashion_e_commerce.ProductCategory.Entities.SubCategory;
import com.fashion_e_commerce.ProductCategory.Repositories.CategoryRepository;
import com.fashion_e_commerce.ProductCategory.Repositories.SubCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private SubCategoryRepository subCategoryRepository;

    // add category
    public Category addCategory(Category category) {
        return categoryRepository.save(category);
    }

    // add subcategory under the category
    public SubCategory addSubCategory(Long categoryId,SubCategory subCategory) {
     Category category = categoryRepository.findById(categoryId)
             .orElseThrow(() -> new RuntimeException("Category not found"));

     subCategory.setCategory(category);
     return subCategoryRepository.save(subCategory);
    }

    // get all the category and thir subcategories
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // get category by id
    public Optional<Category> getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId);
    }

    // get sub-category by id
    public List<SubCategory> getSubCategoriesByCategoryId(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        return category.getSubcategories();
    }

    // Delete category
    public void deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // Remove subcategories associated with the category
        for (SubCategory subCategory : category.getSubcategories()) {
            subCategoryRepository.delete(subCategory);
        }

        // Delete the category itself
        categoryRepository.delete(category);
    }

    // Delete subcategory
    public void deleteSubCategory(Long subCategoryId) {
        SubCategory subCategory = subCategoryRepository.findById(subCategoryId)
                .orElseThrow(() -> new RuntimeException("SubCategory not found"));

        subCategoryRepository.delete(subCategory);
    }


}
