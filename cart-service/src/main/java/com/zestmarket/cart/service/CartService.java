package com.zestmarket.cart.service;

import com.zestmarket.cart.dto.*;
import com.zestmarket.cart.entity.*;
import com.zestmarket.cart.repository.*;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface CartService {
    @Cacheable(value = "carts", key = "#userId")
    CartDTO getCartByUserId(Long userId);

    @CacheEvict(value = "carts", key = "#userId")
    CartDTO addToCart(Long userId, AddToCartRequest request);

    @CacheEvict(value = "carts", key = "#userId")
    CartDTO removeCartItem(Long userId, Long productId);

    @CacheEvict(value = "carts", key = "#userId")
    void clearCart(Long userId);

    void addToWishlist(Long userId, Long productId);
    void removeFromWishlist(Long userId, Long productId);
    List<Long> getWishlist(Long userId);

    Coupon createCoupon(Coupon coupon);
    BigDecimal applyCoupon(String code, BigDecimal orderAmount);
}

@Service
@Transactional
class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final WishlistRepository wishlistRepository;
    private final CouponRepository couponRepository;

    public CartServiceImpl(CartRepository cartRepository,
                           WishlistRepository wishlistRepository,
                           CouponRepository couponRepository) {
        this.cartRepository = cartRepository;
        this.wishlistRepository = wishlistRepository;
        this.couponRepository = couponRepository;
    }

    @Override
    public CartDTO getCartByUserId(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> cartRepository.save(new Cart(userId)));
        return mapToDTO(cart);
    }

    @Override
    public CartDTO addToCart(Long userId, AddToCartRequest request) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> new Cart(userId));

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(request.getProductId()))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
            item.setUnitPrice(request.getUnitPrice());
        } else {
            CartItem item = new CartItem(cart, request.getProductId(), request.getQuantity(), request.getUnitPrice());
            cart.getItems().add(item);
        }

        recalculateTotalPrice(cart);
        return mapToDTO(cartRepository.save(cart));
    }

    @Override
    public CartDTO removeCartItem(Long userId, Long productId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cart.getItems().removeIf(item -> item.getProductId().equals(productId));
        recalculateTotalPrice(cart);
        return mapToDTO(cartRepository.save(cart));
    }

    @Override
    public void clearCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId).orElse(null);
        if (cart != null) {
            cart.getItems().clear();
            cart.setTotalPrice(BigDecimal.ZERO);
            cartRepository.save(cart);
        }
    }

    @Override
    public void addToWishlist(Long userId, Long productId) {
        if (wishlistRepository.findByUserIdAndProductId(userId, productId).isEmpty()) {
            wishlistRepository.save(new Wishlist(userId, productId));
        }
    }

    @Override
    public void removeFromWishlist(Long userId, Long productId) {
        wishlistRepository.deleteByUserIdAndProductId(userId, productId);
    }

    @Override
    public List<Long> getWishlist(Long userId) {
        return wishlistRepository.findByUserId(userId).stream()
                .map(Wishlist::getProductId)
                .collect(Collectors.toList());
    }

    @Override
    public Coupon createCoupon(Coupon coupon) {
        return couponRepository.save(coupon);
    }

    @Override
    public BigDecimal applyCoupon(String code, BigDecimal orderAmount) {
        Coupon coupon = couponRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Invalid coupon code"));

        if (!coupon.isActive()) {
            throw new RuntimeException("Coupon is inactive");
        }

        if (coupon.getMinOrderAmount() != null && orderAmount.compareTo(coupon.getMinOrderAmount()) < 0) {
            throw new RuntimeException("Minimum order amount for coupon is: " + coupon.getMinOrderAmount());
        }

        BigDecimal discount = orderAmount.multiply(coupon.getDiscountPercentage()).divide(new BigDecimal("100"));
        if (coupon.getMaxDiscount() != null && discount.compareTo(coupon.getMaxDiscount()) > 0) {
            discount = coupon.getMaxDiscount();
        }

        return discount;
    }

    private void recalculateTotalPrice(Cart cart) {
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : cart.getItems()) {
            total = total.add(item.getUnitPrice().multiply(new BigDecimal(item.getQuantity())));
        }
        cart.setTotalPrice(total);
    }

    private CartDTO mapToDTO(Cart cart) {
        CartDTO dto = new CartDTO();
        dto.setId(cart.getId());
        dto.setUserId(cart.getUserId());
        dto.setTotalPrice(cart.getTotalPrice());
        dto.setItems(cart.getItems().stream()
                .map(i -> new CartItemDTO(i.getId(), i.getProductId(), i.getQuantity(), i.getUnitPrice()))
                .collect(Collectors.toList()));
        return dto;
    }
}
