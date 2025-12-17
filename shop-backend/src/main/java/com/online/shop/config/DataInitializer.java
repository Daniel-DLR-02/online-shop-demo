package com.online.shop.config;

import com.online.shop.model.entity.Order;
import com.online.shop.model.entity.OrderItem;
import com.online.shop.model.entity.Product;
import com.online.shop.model.enums.OrderStatus;
import com.online.shop.repository.OrderRepository;
import com.online.shop.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Configuration
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public DataInitializer(ProductRepository productRepository,
                           OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public void run(String... args) {

        // Evita recargar datos si ya existen
        if (productRepository.count() > 0) {
            return;
        }

        // --------------------
        // PRODUCTS
        // --------------------
        List<Product> products = List.of(
                createProduct("Laptop Pro 15\"", "LAP-001", 1299),
                createProduct("Mechanical Keyboard", "KEY-002", 149),
                createProduct("Gaming Mouse", "MOU-003", 79),
                createProduct("27\" Monitor 4K", "MON-004", 399),
                createProduct("USB-C Docking Station", "DOC-005", 199),
                createProduct("Noise Cancelling Headphones", "AUD-006", 249),
                createProduct("External SSD 1TB", "SSD-007", 159),
                createProduct("Wireless Mouse", "MOU-008", 39),
                createProduct("Laptop Stand", "ACC-009", 49),
                createProduct("Webcam Full HD", "CAM-010", 89),
                createProduct("Gaming Chair", "CHA-011", 299),
                createProduct("Power Bank 20000mAh", "PWR-012", 59),
                createProduct("Bluetooth Speaker", "SPK-013", 99),
                createProduct("Smartphone Holder", "ACC-014", 19),
                createProduct("Ethernet Adapter USB-C", "NET-015", 29),
                createProduct("Mechanical Numpad", "KEY-016", 69),
                createProduct("Portable Monitor 15\"", "MON-017", 229),
                createProduct("Laptop Backpack", "BAG-018", 89),
                createProduct("HDMI Cable 2m", "CAB-019", 15),
                createProduct("WiFi Router AX3000", "NET-020", 179)
        );

        productRepository.saveAll(products);

        // --------------------
        // ORDERS
        // --------------------
        createOrder(
                "John Doe",
                "john.doe@email.com",
                OrderStatus.CREATED,
                List.of(
                        createItem(products.get(0), 1),
                        createItem(products.get(1), 2)
                )
        );

        createOrder(
                "Alice Smith",
                "alice.smith@email.com",
                OrderStatus.CONFIRMED,
                List.of(
                        createItem(products.get(3), 1),
                        createItem(products.get(6), 1),
                        createItem(products.get(9), 1)
                )
        );

        createOrder(
                "Bob Martin",
                "bob.martin@email.com",
                OrderStatus.SHIPPED,
                List.of(
                        createItem(products.get(10), 1)
                )
        );

        createOrder(
                "Charlie Brown",
                "charlie.brown@email.com",
                OrderStatus.DELIVERED,
                List.of(
                        createItem(products.get(15), 2),
                        createItem(products.get(18), 3)
                )
        );
    }

    // --------------------
    // Helpers
    // --------------------

    private Product createProduct(String name, String sku, double price) {
        Product product = new Product();
        product.setId(UUID.randomUUID());
        product.setName(name);
        product.setSku(sku);
        product.setPrice(BigDecimal.valueOf(price));
        return product;
    }

    private OrderItem createItem(Product product, int quantity) {
        OrderItem item = new OrderItem();
        item.setProductId(product.getId());
        item.setProductName(product.getName());
        item.setProductSku(product.getSku());
        item.setUnitPrice(product.getPrice());
        item.setQuantity(quantity);
        item.setTotalPrice(
                product.getPrice().multiply(BigDecimal.valueOf(quantity))
        );
        return item;
    }

    private void createOrder(String customerName,
                             String customerContact,
                             OrderStatus status,
                             List<OrderItem> items) {

        Order order = new Order();
        order.setId(UUID.randomUUID());
        order.setCustomerName(customerName);
        order.setCustomerContact(customerContact);
        order.setStatus(status);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        items.forEach(item -> item.setOrder(order));
        order.setItems(items);

        BigDecimal totalAmount = items.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalAmount(totalAmount);

        orderRepository.save(order);
    }
}
