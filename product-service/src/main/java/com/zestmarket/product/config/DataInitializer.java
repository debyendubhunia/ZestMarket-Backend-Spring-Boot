package com.zestmarket.product.config;

import com.zestmarket.product.entity.Category;
import com.zestmarket.product.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    public DataInitializer(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) {
        if (categoryRepository.count() == 0) {
            categoryRepository.save(new Category("Electronics", "electronics", "Electronic items and gadgets"));
            categoryRepository.save(new Category("Clothing", "clothing", "Men and Women fashion apparel"));
            categoryRepository.save(new Category("General", "general", "General products"));
        }
    }
}
