package com.sparta.javajyojo.dto;

import com.sparta.javajyojo.entity.Order;
import com.sparta.javajyojo.entity.OrderDetail;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class OrderResponseDto {

    private final Long orderId;
    private final Long userId;
    private final String deliveryRequest;
    private final String address;
    private final String orderStatus;
    private final int totalPrice;
    private final List<OrderDetailDto> orderDetails;

    public OrderResponseDto(Order order, List<OrderDetail> orderDetails) {
        this.orderId = order.getOrderId();
        this.userId = order.getUser().getId();
        this.deliveryRequest = order.getDeliveryRequest();
        this.address = order.getAddress();
        this.orderStatus = order.getOrderStatus().name(); // OrderStatus를 문자열로 변환하여 할당
        this.totalPrice = order.getTotalPrice();
        this.orderDetails = orderDetails.stream()
                .map(OrderDetailDto::new)
                .collect(Collectors.toList());
    }

    @Getter
    public static class OrderDetailDto {
        private final Long menuId;
        private final int amount;

        public OrderDetailDto(OrderDetail orderDetail) {
            this.menuId = orderDetail.getMenu().getMenuId();
            this.amount = orderDetail.getAmount();
        }
    }
}