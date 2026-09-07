package com.zestmarket.cart.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class CartDTO implements Serializable {
    private Long id;
    private Long userId;
    private BigDecimal totalPrice;
    private List<CartItemDTO> items;

    public CartDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }

    public List<CartItemDTO> getItems() { return items; }
    public void setItems(List<CartItemDTO> items) { this.items = items; }
}
