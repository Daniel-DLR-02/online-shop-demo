package com.online.shop.dto.request;

import com.online.shop.model.enums.OrderStatus;
import jakarta.validation.constraints.NotBlank;
import org.jetbrains.annotations.NotNull;

public class UpdateOrderRequest {

    @NotBlank
    private String customerName;

    @NotBlank
    private String customerContact;

    @NotNull
    private OrderStatus status;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerContact() {
        return customerContact;
    }

    public void setCustomerContact(String customerContact) {
        this.customerContact = customerContact;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
