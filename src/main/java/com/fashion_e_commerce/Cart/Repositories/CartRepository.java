package com.fashion_e_commerce.Cart.Repositories;

import com.fashion_e_commerce.Cart.Entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface CartRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByUserId(Long userId);
    @Modifying
    @Query("DELETE FROM CartItem c WHERE c.user.id = :userId AND c.order IS NOT NULL")
    void deleteCartItemsWithOrder(@Param("userId") Long userId);

    Optional<CartItem> findByUserIdAndProductIdAndSize(Long userId, Long productId, String size); // New method

    @Modifying
    @Query("DELETE FROM CartItem ci WHERE ci.product.id = :productId")
    void deleteByProductId(Long productId);
    Optional<CartItem> findByUserIdAndProductId(Long userId, Long productId);}
