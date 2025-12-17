package com.online.shop.config;

import com.online.shop.model.entity.Order;
import com.online.shop.model.entity.OrderItem;
import com.online.shop.model.entity.Product;
import com.online.shop.model.enums.OrderStatus;
import com.online.shop.repository.OrderRepository;
import com.online.shop.repository.ProductRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class OrderDataInitializer {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderDataInitializer(
            OrderRepository orderRepository,
            ProductRepository productRepository
    ) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    @EventListener(ApplicationReadyEvent.class)
    public void initOrders() {

        if (orderRepository.count() > 0) {
            return;
        }

        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            return;
        }

        for (int i = 0; i < 5; i++) {

            Product product = products.get(i);

            Order order = new Order();
            order.setCustomerName("Customer " + (i + 1));
            order.setCustomerContact("customer" + (i + 1) + "@email.com");
            order.setStatus(OrderStatus.CREATED);
            order.setCreatedAt(LocalDateTime.now().minusDays(i + 1));
            order.setUpdatedAt(LocalDateTime.now().minusDays(i + 1));

            OrderItem item = new OrderItem();
            item.setProductId(product.getId());
            item.setProductSku(product.getSku());
            item.setProductName(product.getName());
            item.setUnitPrice(product.getPrice());
            item.setQuantity(i + 1);
            item.setTotalPrice(
                    product.getPrice().multiply(BigDecimal.valueOf(i + 1))
            );
            item.setOrder(order);

            order.setItems(List.of(item));
            order.setTotalAmount(item.getTotalPrice());

            orderRepository.save(order);
        }
    }
}
