package com.zestmarket.cart.controller;

import com.zestmarket.cart.entity.Coupon;
import com.zestmarket.cart.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/wishlist")
@Tag(name = "Wishlist APIs", description = "User Wishlist Management")
public class WishlistController {

    private final CartService cartService;

    public WishlistController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    @Operation(summary = "Get user wishlist product IDs")
    public ResponseEntity<List<Long>> getWishlist(@RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(cartService.getWishlist(userId));
    }

    @PostMapping("/{productId}")
    @Operation(summary = "Add product to wishlist")
    public ResponseEntity<Void> addToWishlist(@RequestHeader("X-User-Id") Long userId, @PathVariable Long productId) {
        cartService.addToWishlist(userId, productId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "Remove product from wishlist")
    public ResponseEntity<Void> removeFromWishlist(@RequestHeader("X-User-Id") Long userId, @PathVariable Long productId) {
        cartService.removeFromWishlist(userId, productId);
        return ResponseEntity.ok().build();
    }
}
