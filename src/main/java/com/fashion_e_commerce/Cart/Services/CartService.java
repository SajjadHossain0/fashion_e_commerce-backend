package com.fashion_e_commerce.Cart.Services;

import com.fashion_e_commerce.Cart.Entities.CartItem;
import com.fashion_e_commerce.Cart.Repositories.CartRepository;
import com.fashion_e_commerce.ProductDetails.Entities.Product;
import com.fashion_e_commerce.ProductDetails.Repositories.ProductRepository;
import com.fashion_e_commerce.User.Entities.User;
import com.fashion_e_commerce.User.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public CartItem addToCart(Long userId, Long productId, int quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));


        Optional<CartItem> existingCartItem = cartRepository.findByUserIdAndProductId(userId, productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (existingCartItem.isPresent()) {
            CartItem item = existingCartItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            item.setTotalprice(item.getQuantity() * product.getDiscountedPrice());
            return cartRepository.save(item);
        }
        else {
            CartItem newItem = new CartItem();
            newItem.setUser(user);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
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

    public void clearCart(Long userId) {
        cartRepository.deleteAll(cartRepository.findByUserId(userId));
    }
}
