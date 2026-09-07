package com.zestmarket.product.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "product_images")
public class ProductImage implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String imageUrl;

    private boolean primaryImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    public ProductImage() {}

    public ProductImage(String imageUrl, boolean primaryImage, Product product) {
        this.imageUrl = imageUrl;
        this.primaryImage = primaryImage;
        this.product = product;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public boolean isPrimaryImage() { return primaryImage; }
    public void setPrimaryImage(boolean primaryImage) { this.primaryImage = primaryImage; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
}
