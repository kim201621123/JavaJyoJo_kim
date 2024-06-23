package com.sparta.javajyojo.dto;

import com.sparta.javajyojo.enums.OrderStatus;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class OrderRequestDto {
    private String deliveryRequest;
    private String address;
    private List<OrderDetailDto> orderDetails;
    private OrderStatus orderStatus;

    // Getter for orderDetails
    public List<OrderDetailDto> getOrderDetails() {
        return orderDetails;
    }

    // Setter for orderDetails
    public void setOrderDetails(List<OrderDetailDto> orderDetails) {
        this.orderDetails = orderDetails;
    }

    @Getter
    public static class OrderDetailDto {
        private Long menuId;
        private int amount;
    }
}