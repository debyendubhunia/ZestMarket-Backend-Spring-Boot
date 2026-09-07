package com.zestmarket.cart.controller;

import com.zestmarket.cart.entity.Coupon;
import com.zestmarket.cart.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/coupons")
@Tag(name = "Coupon APIs", description = "Coupon creation & discount calculation")
public class CouponController {

    private final CartService cartService;

    public CouponController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping
    @Operation(summary = "Admin API: Create Coupon")
    public ResponseEntity<Coupon> createCoupon(@RequestBody Coupon coupon) {
        return ResponseEntity.ok(cartService.createCoupon(coupon));
    }

    @PostMapping("/apply")
    @Operation(summary = "Apply coupon code and calculate discount amount")
    public ResponseEntity<BigDecimal> applyCoupon(@RequestParam String code, @RequestParam BigDecimal orderAmount) {
        return ResponseEntity.ok(cartService.applyCoupon(code, orderAmount));
    }
}
