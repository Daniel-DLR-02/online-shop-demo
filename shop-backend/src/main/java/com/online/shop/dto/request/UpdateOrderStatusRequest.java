package com.online.shop.dto.request;

import com.online.shop.model.enums.OrderStatus;

public class UpdateOrderStatusRequest {

    private OrderStatus status;

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}