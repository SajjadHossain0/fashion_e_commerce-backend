package com.fashion_e_commerce.Wishlist.Repositories;

import com.fashion_e_commerce.Wishlist.Entities.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    List<Wishlist> findByUserId(Long userId);
    Optional<Wishlist> findByUserIdAndProductId(Long userId, Long productId);
}
