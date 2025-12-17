package com.online.shop.service;

import com.online.shop.dto.request.CreateOrderRequest;
import com.online.shop.dto.request.UpdateOrderStatusRequest;
import com.online.shop.dto.response.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    OrderResponse createOrder(CreateOrderRequest request);

    Page<OrderResponse> getAllOrders(Pageable pageable);

    OrderResponse getOrderById(UUID orderId);

    OrderResponse updateOrderStatus(UUID orderId, UpdateOrderStatusRequest request);
}
