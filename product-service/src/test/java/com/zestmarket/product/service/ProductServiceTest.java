package com.zestmarket.product.service;

import com.zestmarket.product.dto.ProductDTO;
import com.zestmarket.product.dto.ProductRequest;
import com.zestmarket.product.entity.Product;
import com.zestmarket.product.repository.CategoryRepository;
import com.zestmarket.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetProductByIdSuccess() {
        Product product = new Product();
        product.setId(100L);
        product.setTitle("Laptop");
        product.setSku("LAP-123");
        product.setPrice(new BigDecimal("999.99"));

        when(productRepository.findById(100L)).thenReturn(Optional.of(product));

        ProductDTO dto = productService.getProductById(100L);

        assertNotNull(dto);
        assertEquals("Laptop", dto.getTitle());
        assertEquals("LAP-123", dto.getSku());
    }

    @Test
    void testCreateProductSuccess() {
        ProductRequest request = new ProductRequest();
        request.setTitle("Smartphone");
        request.setSku("PHONE-001");
        request.setPrice(new BigDecimal("499.99"));
        request.setStockQuantity(50);

        Product savedProduct = new Product();
        savedProduct.setId(1L);
        savedProduct.setTitle("Smartphone");
        savedProduct.setSku("PHONE-001");
        savedProduct.setPrice(new BigDecimal("499.99"));
        savedProduct.setStockQuantity(50);

        when(productRepository.save(any())).thenReturn(savedProduct);

        ProductDTO dto = productService.createProduct(request);

        assertNotNull(dto);
        assertEquals("Smartphone", dto.getTitle());
        verify(productRepository, times(1)).save(any());
    }
}
