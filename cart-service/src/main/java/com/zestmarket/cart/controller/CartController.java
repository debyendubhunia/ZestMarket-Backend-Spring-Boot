package com.zestmarket.cart.controller;

import com.zestmarket.cart.dto.*;
import com.zestmarket.cart.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@Tag(name = "Cart Management APIs", description = "Shopping Cart Operations")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    @Operation(summary = "Get user shopping cart")
    public ResponseEntity<CartDTO> getCart(@RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(cartService.getCartByUserId(userId));
    }

    @PostMapping("/items")
    @Operation(summary = "Add item to shopping cart")
    public ResponseEntity<CartDTO> addToCart(@RequestHeader("X-User-Id") Long userId, @Valid @RequestBody AddToCartRequest request) {
        return ResponseEntity.ok(cartService.addToCart(userId, request));
    }

    @DeleteMapping("/items/{productId}")
    @Operation(summary = "Remove item from cart")
    public ResponseEntity<CartDTO> removeCartItem(@RequestHeader("X-User-Id") Long userId, @PathVariable Long productId) {
        return ResponseEntity.ok(cartService.removeCartItem(userId, productId));
    }

    @DeleteMapping("/clear")
    @Operation(summary = "Clear shopping cart")
    public ResponseEntity<Void> clearCart(@RequestHeader("X-User-Id") Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.ok().build();
    }
}
