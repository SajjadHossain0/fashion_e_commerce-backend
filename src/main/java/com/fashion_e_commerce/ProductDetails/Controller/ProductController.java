package com.fashion_e_commerce.ProductDetails.Controller;

import com.fashion_e_commerce.ProductDetails.Entities.Product;
import com.fashion_e_commerce.ProductDetails.Services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@CrossOrigin
public class ProductController {

    @Autowired
    private ProductService productService;

    // Add a new product
    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestParam("title") String title, @RequestParam("price") double price,
                                              @RequestParam("discount") double discount, @RequestParam("description") String description,
                                              @RequestParam("detailedDescription") String detailedDescription, @RequestParam("brand") String brand,
                                              @RequestParam("categoryId") Long categoryId, @RequestParam("subCategoryId") Long subCategoryId,
                                              @RequestParam("sizes") List<String> sizes, @RequestParam("material") String material,
                                              @RequestParam("stock") int stock, @RequestParam("available") boolean available, @RequestParam("image") MultipartFile image,
                                              @RequestParam("tags") List<String> tags) throws IOException {

        Product product = new Product();
        product.setTitle(title);
        product.setPrice(price);
        product.setDiscount(discount);
        product.setDescription(description);
        product.setDetaileddescription(detailedDescription);
        product.setBrand(brand);
        product.setSizes(sizes);
        product.setMaterial(material);
        product.setStock(stock);
        product.setAvailable(available);
        product.setImage(image.getBytes());
        product.setTags(tags); // Set the tags

        Product savedProduct = productService.addProduct(product, categoryId, subCategoryId, tags);
        return ResponseEntity.ok(savedProduct);
    }

    // Get all products
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }


    // Get a single product by ID
    @GetMapping("/{productId}")
    public ResponseEntity<Optional<Product>> getProductById(@PathVariable Long productId) {
        Optional<Product> product = productService.getProductById(productId);
        return ResponseEntity.ok(product);
    }

    // Update an existing product
/*    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long productId, @RequestParam("title") String title, @RequestParam("price") double price,
                                                 @RequestParam("discount") double discount, @RequestParam("description") String description,
                                                 @RequestParam("detailedDescription") String detailedDescription, @RequestParam("brand") String brand,
                                                 @RequestParam("categoryId") Long categoryId, @RequestParam("subCategoryId") Long subCategoryId,
                                                 @RequestParam("sizes") List<String> sizes, @RequestParam("material") String material,
                                                 @RequestParam("stock") int stock, @RequestParam("available") boolean available,
                                                 @RequestParam("image") MultipartFile image, @RequestParam("tags") List<String> tags) throws IOException {

        Product updatedProduct = productService.updateProduct(
                productId, title, price, discount, description, detailedDescription, brand,
                categoryId, subCategoryId, sizes, material, stock, available, image.getBytes(), tags);
        return ResponseEntity.ok(updatedProduct);
    }*/
    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long productId,
                                                 @RequestParam(value = "title", required = false) String title,
                                                 @RequestParam(value = "price", required = false) Double price,
                                                 @RequestParam(value = "discount", required = false) Double discount,
                                                 @RequestParam(value = "description", required = false) String description,
                                                 @RequestParam(value = "detailedDescription", required = false) String detailedDescription,
                                                 @RequestParam(value = "sizes", required = false) List<String> sizes,
                                                 @RequestParam(value = "stock", required = false) Integer stock,
                                                 @RequestParam(value = "image", required = false) MultipartFile image,
                                                 @RequestParam(value = "tags", required = false) List<String> tags,
                                                 @RequestParam("available") boolean available) throws IOException {

        byte[] imageBytes = (image != null) ? image.getBytes() : null;  // Handle image upload

        Product updatedProduct = productService.updateProduct(
                productId, title, price != null ? price : 0.0, discount != null ? discount : 0.0, description,
                detailedDescription, sizes, stock != null ? stock : 0, available, imageBytes, tags);

        return ResponseEntity.ok(updatedProduct);
    }



    // Delete a product by ID
    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
        String response = productService.deleteProduct(productId);
        return ResponseEntity.ok(response);
    }

}
