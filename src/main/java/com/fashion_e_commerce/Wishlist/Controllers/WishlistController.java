package com.fashion_e_commerce.Wishlist.Controllers;

import com.fashion_e_commerce.Wishlist.Entities.Wishlist;
import com.fashion_e_commerce.Wishlist.Services.WishlistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
@CrossOrigin
public class WishlistController {
    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @PostMapping("/{userId}/{productId}")
    public ResponseEntity<Wishlist> addToWishlist(@PathVariable Long userId, @PathVariable Long productId) {
        return ResponseEntity.ok(wishlistService.addToWishlist(userId, productId));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Wishlist>> getWishlist(@PathVariable Long userId) {
        return ResponseEntity.ok(wishlistService.getWishlist(userId));
    }

    @DeleteMapping("/{userId}/{productId}")
    public ResponseEntity<Void> removeFromWishlist(@PathVariable Long userId, @PathVariable Long productId) {
        wishlistService.removeFromWishlist(userId, productId);
        return ResponseEntity.ok().build();
    }
}
