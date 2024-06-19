package com.sparta.javajyojo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderRequestDto {
    private String deliveryRequest;
    private String address;
    private List<OrderDetailDto> orderDetails;

    @Getter
    @Setter
    public static class OrderDetailDto {
        private Long menuId;
        private int amount;
    }
}