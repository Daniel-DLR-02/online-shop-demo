package com.online.shop.service.impl;

import com.online.shop.dto.request.CreateOrderRequest;
import com.online.shop.dto.request.OrderItemRequest;
import com.online.shop.dto.request.UpdateOrderRequest;
import com.online.shop.dto.request.UpdateOrderStatusRequest;
import com.online.shop.dto.response.OrderItemResponse;
import com.online.shop.dto.response.OrderResponse;
import com.online.shop.exception.BusinessException;
import com.online.shop.exception.InvalidOrderStateException;
import com.online.shop.exception.NotFoundException;
import com.online.shop.exception.OrderNotFoundException;
import com.online.shop.model.entity.Order;
import com.online.shop.model.entity.OrderItem;
import com.online.shop.model.entity.Product;
import com.online.shop.model.enums.OrderStatus;
import com.online.shop.repository.OrderRepository;
import com.online.shop.repository.ProductRepository;
import com.online.shop.service.OrderService;
import jakarta.transaction.Transactional;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderServiceImpl(
            OrderRepository orderRepository,
            ProductRepository productRepository
    ) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }
    // 1️⃣ CREATE ORDER
    @Override
    public OrderResponse createOrder(CreateOrderRequest request) {

        Order order = new Order();
        order.setCustomerName(request.getCustomerName());
        order.setCustomerContact(request.getCustomerContact());
        order.setStatus(OrderStatus.CREATED);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        List<OrderItem> items = new ArrayList<>();

        for (OrderItemRequest itemRequest : request.getItems()) {
            items.add(mapToOrderItem(itemRequest, order));
        }

        BigDecimal totalAmount = items.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setItems(items);
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);

        return mapToOrderResponse(savedOrder);
    }

    // 2️⃣ GET ALL ORDERS
    @Override
    public Page<OrderResponse> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(this::mapToOrderResponse);
    }

    // 3️⃣ GET ORDER BY ID
    @Override
    public OrderResponse getOrderById(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        return mapToOrderResponse(order);
    }

    // 4️⃣ UPDATE ORDER STATUS
    @Override
    public OrderResponse updateOrderStatus(UUID orderId, UpdateOrderStatusRequest request) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        validateStatusTransition(order.getStatus(), request.getStatus());

        order.setStatus(request.getStatus());
        order.setUpdatedAt(LocalDateTime.now());

        Order updatedOrder = orderRepository.save(order);

        return mapToOrderResponse(updatedOrder);
    }

    @Override
    @Transactional
    public OrderResponse updateOrder(UUID id, UpdateOrderRequest request) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found"));

        if (order.isFinalState()) {
            throw new BusinessException("Order is in a final state and cannot be modified");
        }

        order.setCustomerName(request.getCustomerName());
        order.setCustomerContact(request.getCustomerContact());

        if (request.getStatus() != order.getStatus()) {
            validateStatusTransition(order.getStatus(), request.getStatus());
            order.setStatus(request.getStatus());
        }

        order.setUpdatedAt(LocalDateTime.now());

        Order saved = orderRepository.save(order);

        return mapToResponse(saved);
    }

    // -----------------------
    // Helpers
    // -----------------------

    private OrderItem mapToOrderItem(OrderItemRequest request, Order order) {

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        OrderItem item = new OrderItem();
        item.setProductId(product.getId());
        item.setProductSku(product.getSku());
        item.setProductName(product.getName());
        item.setUnitPrice(product.getPrice());
        item.setQuantity(request.getQuantity());
        item.setTotalPrice(
                product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity()))
        );
        item.setOrder(order);

        return item;
    }

    private OrderResponse mapToOrderResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setCustomerName(order.getCustomerName());
        response.setCustomerContact(order.getCustomerContact());
        response.setStatus(order.getStatus());
        response.setTotalAmount(order.getTotalAmount());
        response.setCreatedAt(order.getCreatedAt());

        List<OrderItemResponse> items = order.getItems().stream()
                .map(this::mapToOrderItemResponse)
                .toList();

        response.setItems(items);
        return response;
    }

    private OrderItemResponse mapToOrderItemResponse(OrderItem item) {
        OrderItemResponse response = new OrderItemResponse();
        response.setProductSku(item.getProductSku());
        response.setProductName(item.getProductName());
        response.setUnitPrice(item.getUnitPrice());
        response.setQuantity(item.getQuantity());
        response.setTotalPrice(item.getTotalPrice());
        return response;
    }

    private OrderResponse mapToResponse(Order order) {

        List<OrderItemResponse> items = order.getItems().stream()
                .map(item -> {
                    OrderItemResponse response = new OrderItemResponse();
                    response.setProductSku(item.getProductSku());
                    response.setProductName(item.getProductName());
                    response.setUnitPrice(item.getUnitPrice());
                    response.setQuantity(item.getQuantity());
                    response.setTotalPrice(item.getTotalPrice());
                    return response;
                })
                .toList();

        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setCustomerName(order.getCustomerName());
        response.setCustomerContact(order.getCustomerContact());
        response.setStatus(order.getStatus());
        response.setTotalAmount(order.getTotalAmount());
        response.setCreatedAt(order.getCreatedAt());
        response.setItems(items);

        return response;
    }





    // -----------------------
    // Business rules
    // -----------------------

    private void validateStatusTransition(OrderStatus current, OrderStatus next) {

        if (current == OrderStatus.DELIVERED ||
                current == OrderStatus.CANCELLED ||
                current == OrderStatus.RETURNED) {
            throw new InvalidOrderStateException(
                    "Order is in a final state and cannot be modified"
            );
        }

        switch (current) {
            case CREATED -> {
                if (next != OrderStatus.CONFIRMED && next != OrderStatus.CANCELLED) {
                    throw new InvalidOrderStateException("Invalid transition from CREATED");
                }
            }
            case CONFIRMED -> {
                if (next != OrderStatus.SHIPPED) {
                    throw new InvalidOrderStateException("Invalid transition from CONFIRMED");
                }
            }
            case SHIPPED -> {
                if (next != OrderStatus.DELIVERED && next != OrderStatus.RETURNED) {
                    throw new InvalidOrderStateException("Invalid transition from SHIPPED");
                }
            }
        }
    }


}
