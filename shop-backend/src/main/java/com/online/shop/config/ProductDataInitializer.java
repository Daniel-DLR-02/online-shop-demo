package com.online.shop.config;

import com.online.shop.model.entity.Product;
import com.online.shop.repository.ProductRepository;
import org.springframework.context.event.EventListener;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
public class ProductDataInitializer {

    private final ProductRepository productRepository;

    public ProductDataInitializer(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    @EventListener(ApplicationReadyEvent.class)
    public void initProducts() {

        if (productRepository.count() > 0) {
            return;
        }

        for (int i = 1; i <= 20; i++) {
            Product product = new Product();
            product.setSku("SKU-" + i);
            product.setName("Product " + i);
            product.setPrice(BigDecimal.valueOf(10 + i));

            productRepository.save(product);
        }
    }
}
