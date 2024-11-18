package com.fashion_e_commerce.ProductCategory.Controllers;

import com.fashion_e_commerce.ProductCategory.Entities.*;
import com.fashion_e_commerce.ProductCategory.Services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;
import java.io.IOException;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public ResponseEntity<Category> addCategory(@RequestParam("name") String name,
                                                @RequestParam("image") MultipartFile image) throws IOException {
    Category category = new Category();
    category.setName(name);
    category.setImage(image.getBytes());

    Category savedCategory = categoryService.addCategory(category);

    return ResponseEntity.ok(savedCategory);
    }

    // Add a new subcategory under a category with image upload
    @PostMapping("/{categoryId}/subcategories")
    public ResponseEntity<SubCategory> addSubcategory(
            @PathVariable Long categoryId,
            @RequestParam("name") String name,
            @RequestParam("image") MultipartFile image) throws IOException {

        SubCategory subcategory = new SubCategory();
        subcategory.setName(name);
        subcategory.setImage(image.getBytes());

        SubCategory savedSubcategory = categoryService.addSubCategory(categoryId, subcategory);
        return ResponseEntity.ok(savedSubcategory);
    }

    // Get all categories with their subcategories
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    // Get a single category by ID
    @GetMapping("/{categoryId}")
    public ResponseEntity<Optional<Category>> getCategoryById(@PathVariable Long categoryId) {
        Optional<Category> category = categoryService.getCategoryById(categoryId);
        return ResponseEntity.ok(category);
    }

    // Get subcategories by category ID
    @GetMapping("/{categoryId}/subcategories")
    public ResponseEntity<List<SubCategory>> getSubcategoriesByCategoryId(@PathVariable Long categoryId) {
        List<SubCategory> subcategories = categoryService.getSubCategoriesByCategoryId(categoryId);
        return ResponseEntity.ok(subcategories);
    }

    // Delete category by ID
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok("Category deleted successfully");
    }

    // Delete subcategory by ID
    @DeleteMapping("/subcategories/{subCategoryId}")
    public ResponseEntity<String> deleteSubCategory(@PathVariable Long subCategoryId) {
        categoryService.deleteSubCategory(subCategoryId);
        return ResponseEntity.ok("SubCategory deleted successfully");
    }

}
