package com.fashion_e_commerce.Cart.Services;

import com.fashion_e_commerce.Cart.Entities.CartItem;
import com.fashion_e_commerce.Cart.Repositories.CartRepository;
import com.fashion_e_commerce.ProductDetails.Entities.Product;
import com.fashion_e_commerce.ProductDetails.Repositories.ProductRepository;
import com.fashion_e_commerce.User.Entities.User;
import com.fashion_e_commerce.User.Repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;

    public CartItem addToCart(Long userId, Long productId, int quantity, String size) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Check if the size is valid
        if (!product.getSizes().contains(size)) {
            throw new RuntimeException("Invalid size selected for this product");
        }

        // Check if an item with the same product and size already exists in the cart
        Optional<CartItem> existingCartItem = cartRepository.findByUserIdAndProductIdAndSize(userId, productId, size);

        if (existingCartItem.isPresent()) {
            CartItem item = existingCartItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            item.setTotalprice(item.getQuantity() * product.getDiscountedPrice());
            return cartRepository.save(item);
        } else {
            CartItem newItem = new CartItem();
            newItem.setUser(user);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            newItem.setSize(size); // Set the size
            newItem.setTotalprice(quantity * product.getDiscountedPrice());
            return cartRepository.save(newItem);
        }
    }

    public List<CartItem> getCartItems(Long userId) {
        return cartRepository.findByUserId(userId);
    }


    public void removeItem(Long cartItemId) {
        cartRepository.deleteById(cartItemId);
    }

    /*public void clearCart(Long userId) {
        List<CartItem> cartItems = cartRepository.findByUserId(userId);
        cartRepository.deleteAll(cartItems);
    }*/
    @Transactional
    public void clearCart(Long userId) {
        cartRepository.deleteCartItemsWithOrder(userId);
    }

}
