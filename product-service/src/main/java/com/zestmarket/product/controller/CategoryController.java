package com.zestmarket.product.controller;

import com.zestmarket.product.dto.CategoryDTO;
import com.zestmarket.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@Tag(name = "Category APIs", description = "Category Management Endpoints")
public class CategoryController {

    private final ProductService productService;

    public CategoryController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @Operation(summary = "Admin API: Create Category")
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO request) {
        return ResponseEntity.ok(productService.createCategory(request));
    }

    @GetMapping
    @Operation(summary = "List all categories")
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        return ResponseEntity.ok(productService.getAllCategories());
    }
}
