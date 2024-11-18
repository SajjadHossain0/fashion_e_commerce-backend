package com.fashion_e_commerce.ProductDetails.Services;

import com.fashion_e_commerce.Cart.Repositories.CartRepository;
import com.fashion_e_commerce.Order.Repositories.OrderRepository;
import com.fashion_e_commerce.ProductCategory.Entities.Category;
import com.fashion_e_commerce.ProductCategory.Entities.SubCategory;
import com.fashion_e_commerce.ProductCategory.Repositories.CategoryRepository;
import com.fashion_e_commerce.ProductCategory.Repositories.SubCategoryRepository;
import com.fashion_e_commerce.ProductDetails.Entities.Product;
import com.fashion_e_commerce.ProductDetails.Repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private OrderRepository orderRepository;

    // Add a new product with tags
    public Product addProduct(Product product, Long categoryId, Long subCategoryId, List<String> tags) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        SubCategory subCategory = subCategoryRepository.findById(subCategoryId)
                .orElseThrow(() -> new RuntimeException("SubCategory not found"));

        product.setCategory(category);
        product.setSubCategory(subCategory);
        product.setTags(tags); // Set the tags

        return productRepository.save(product);
    }

    // Get all products
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Get product by ID
    public Optional<Product> getProductById(Long productId) {
        return productRepository.findById(productId);
    }

    // Update a product with tags
/*
    public Product updateProduct(Long productId, String title, double price, double discount, String description,
                                 String detailedDescription, String brand, Long categoryId, Long subCategoryId,
                                 List<String> sizes, String material, int stock, boolean available, byte[] image,
                                 List<String> tags) throws IOException {

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
        product.setTags(tags); // Update the tags

        return productRepository.save(product);
    }
*/
    // Update a product with specific fields
    public Product updateProduct(Long productId, String title, double price, double discount, String description,
                                 String detailedDescription, List<String> sizes, int stock, boolean available,
                                 byte[] image, List<String> tags) throws IOException {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Update only the provided fields
        if (title != null) product.setTitle(title);
        if (price >= 0) product.setPrice(price);
        if (discount >= 0) product.setDiscount(discount);
        if (description != null) product.setDescription(description);
        if (detailedDescription != null) product.setDetaileddescription(detailedDescription);
        if (sizes != null) product.setSizes(sizes);
        if (stock >= 0) product.setStock(stock);
        if (image != null) product.setImage(image);
        if (tags != null) product.setTags(tags);

        // Ensure other fields like available or category are not modified if not provided
        product.setAvailable(available);  // Assuming availability should always be updated

        return productRepository.save(product);
    }


    // Delete a product
    @Transactional
    public String deleteProduct(Long productId) {
        cartRepository.deleteByProductId(productId);
        productRepository.deleteById(productId);
        return "Product deleted successfully";
    }

    public List<Product> globalSearch(String keyword) {
        return productRepository.searchProducts(keyword);
    }

    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    // Fetch products by subcategory ID
    public List<Product> getProductsBySubCategory(Long subCategoryId) {
        return productRepository.findBySubCategoryId(subCategoryId);
    }

}

