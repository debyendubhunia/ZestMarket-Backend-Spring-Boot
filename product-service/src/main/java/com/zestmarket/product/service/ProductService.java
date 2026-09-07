package com.zestmarket.product.service;

import com.zestmarket.product.dto.*;
import com.zestmarket.product.entity.*;
import com.zestmarket.product.repository.*;
import com.zestmarket.product.specification.ProductSpecification;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public interface ProductService {
    ProductDTO createProduct(ProductRequest request);
    @Cacheable(value = "products", key = "#id")
    ProductDTO getProductById(Long id);
    Page<ProductDTO> searchProducts(String search, Long categoryId, BigDecimal minPrice, BigDecimal maxPrice, Boolean active, Pageable pageable);
    @CacheEvict(value = "products", key = "#id")
    ProductDTO updateProduct(Long id, ProductRequest request);
    @CacheEvict(value = "products", key = "#id")
    void deleteProduct(Long id);
    String uploadProductImage(Long productId, MultipartFile file);
    
    CategoryDTO createCategory(CategoryDTO request);
    List<CategoryDTO> getAllCategories();
}

@Service
@Transactional
class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public ProductDTO createProduct(ProductRequest request) {
        Product product = new Product();
        product.setTitle(request.getTitle());
        product.setSku(request.getSku());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setActive(request.isActive());

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseGet(() -> categoryRepository.findAll().stream().findFirst()
                            .orElseGet(() -> categoryRepository.save(new Category("General", "general", "General Category"))));
            product.setCategory(category);
        }

        Product saved = productRepository.save(product);
        return mapToDTO(saved);
    }

    @Override
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        return mapToDTO(product);
    }

    @Override
    public Page<ProductDTO> searchProducts(String search, Long categoryId, BigDecimal minPrice, BigDecimal maxPrice, Boolean active, Pageable pageable) {
        Specification<Product> spec = ProductSpecification.filterProducts(search, categoryId, minPrice, maxPrice, active);
        return productRepository.findAll(spec, pageable).map(this::mapToDTO);
    }

    @Override
    public ProductDTO updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        product.setTitle(request.getTitle());
        product.setSku(request.getSku());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setActive(request.isActive());

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseGet(() -> categoryRepository.findAll().stream().findFirst()
                            .orElseGet(() -> categoryRepository.save(new Category("General", "general", "General Category"))));
            product.setCategory(category);
        }

        return mapToDTO(productRepository.save(product));
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public String uploadProductImage(Long productId, MultipartFile file) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        try {
            String uploadDir = "uploads/products/";
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            String filePath = uploadDir + fileName;
            file.transferTo(new File(filePath));

            ProductImage image = new ProductImage(filePath, product.getImages().isEmpty(), product);
            product.getImages().add(image);
            productRepository.save(product);

            return filePath;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image: " + e.getMessage());
        }
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO request) {
        String slug = request.getSlug();
        if (slug == null || slug.isBlank()) {
            String base = (request.getName() != null && !request.getName().isBlank())
                    ? request.getName().toLowerCase().trim().replaceAll("[^a-z0-9]+", "-").replaceAll("^-|-$", "")
                    : "category";
            slug = base.isEmpty() ? "category" : base;
        }

        if (categoryRepository.existsBySlug(slug)) {
            slug = slug + "-" + (System.currentTimeMillis() % 10000);
        }

        Category category = new Category(request.getName() != null ? request.getName() : "Unnamed Category", slug, request.getDescription());
        Category saved = categoryRepository.save(category);
        return new CategoryDTO(saved.getId(), saved.getName(), saved.getSlug(), saved.getDescription());
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(c -> new CategoryDTO(c.getId(), c.getName(), c.getSlug(), c.getDescription()))
                .collect(Collectors.toList());
    }

    private ProductDTO mapToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setTitle(product.getTitle());
        dto.setSku(product.getSku());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStockQuantity(product.getStockQuantity());
        dto.setActive(product.isActive());

        if (product.getCategory() != null) {
            dto.setCategoryId(product.getCategory().getId());
            dto.setCategoryName(product.getCategory().getName());
        }

        if (product.getImages() != null) {
            dto.setImageUrls(product.getImages().stream().map(ProductImage::getImageUrl).collect(Collectors.toList()));
        }

        return dto;
    }
}
