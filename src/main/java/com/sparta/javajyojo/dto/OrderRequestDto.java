package com.sparta.javajyojo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class OrderRequestDto {
    private String deliveryRequest;
    private String address;
    private List<OrderDetailDto> orderDetails;

    @Getter
    public static class OrderDetailDto {
        private Long menuId;
        private int amount;
    }
}