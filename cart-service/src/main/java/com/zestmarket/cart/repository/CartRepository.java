package com.zestmarket.cart.repository;

import com.zestmarket.cart.entity.Cart;
import com.zestmarket.cart.entity.Coupon;
import com.zestmarket.cart.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserId(Long userId);
}
