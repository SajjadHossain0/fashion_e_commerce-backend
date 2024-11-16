package com.fashion_e_commerce.Wishlist.Services;

import com.fashion_e_commerce.Cart.Entities.CartItem;
import com.fashion_e_commerce.ProductDetails.Entities.Product;
import com.fashion_e_commerce.ProductDetails.Repositories.ProductRepository;
import com.fashion_e_commerce.User.Entities.User;
import com.fashion_e_commerce.User.Repositories.UserRepository;
import com.fashion_e_commerce.Wishlist.Entities.Wishlist;
import com.fashion_e_commerce.Wishlist.Repositories.WishlistRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WishlistService {
    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public WishlistService(WishlistRepository wishlistRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.wishlistRepository = wishlistRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public Wishlist addToWishlist(Long userId, Long productId) {
        if (wishlistRepository.findByUserIdAndProductId(userId, productId).isPresent()) {
            throw new IllegalStateException("Product already in wishlist");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Wishlist wishlist = new Wishlist();
        wishlist.setUser(new User(userId)); // Assuming User entity has this constructor
        wishlist.setProduct(new Product(productId)); // Assuming Product entity has this constructor
        return wishlistRepository.save(wishlist);
    }

    public List<Wishlist> getWishlist(Long userId) {
        return wishlistRepository.findByUserId(userId);
    }

    public void removeFromWishlist(Long userId, Long productId) {
        Wishlist wishlist = wishlistRepository.findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new IllegalStateException("Wishlist item not found"));
        wishlistRepository.delete(wishlist);
    }
}
