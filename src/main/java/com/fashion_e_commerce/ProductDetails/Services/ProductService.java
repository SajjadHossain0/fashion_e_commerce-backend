package com.fashion_e_commerce.ProductDetails.Services;

import com.fashion_e_commerce.ProductCategory.Entities.Category;
import com.fashion_e_commerce.ProductCategory.Entities.SubCategory;
import com.fashion_e_commerce.ProductCategory.Repositories.CategoryRepository;
import com.fashion_e_commerce.ProductCategory.Repositories.SubCategoryRepository;
import com.fashion_e_commerce.ProductDetails.Entities.Product;
import com.fashion_e_commerce.ProductDetails.Repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    public Product addProduct(Product product, Long categoryId, Long subCategoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        SubCategory subCategory = subCategoryRepository.findById(subCategoryId)
                .orElseThrow(() -> new RuntimeException("SubCategory not found"));

        product.setCategory(category);
        product.setSubCategory(subCategory);

        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long productId) {
        return productRepository.findById(productId);
    }

    public Product updateProduct(Long productId, String title, double price, double discount, String description,
                                 String detailedDescription, String brand, Long categoryId, Long subCategoryId,
                                 List<String> sizes, String material, int stock, boolean available, byte[] image) throws IOException {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        SubCategory subCategory = subCategoryRepository.findById(subCategoryId)
                .orElseThrow(() -> new RuntimeException("SubCategory not found"));

        product.setTitle(title);
        product.setPrice(price);
        product.setDiscount(discount);
        product.setDescription(description);
        product.setDetaileddescription(detailedDescription);
        product.setBrand(brand);
        product.setCategory(category);
        product.setSubCategory(subCategory);
        product.setSizes(sizes);
        product.setMaterial(material);
        product.setStock(stock);
        product.setAvailable(available);
        product.setImage(image);

        return productRepository.save(product);
    }

    public String deleteProduct(Long productId) {
        productRepository.deleteById(productId);
        return "Product deleted successfully";
    }
}
