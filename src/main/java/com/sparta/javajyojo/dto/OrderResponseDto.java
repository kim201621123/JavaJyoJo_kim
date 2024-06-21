package com.sparta.javajyojo.dto;

import com.sparta.javajyojo.entity.Order;
import com.sparta.javajyojo.entity.OrderDetail;
import lombok.Getter;
import org.hibernate.Hibernate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
public class OrderResponseDto {

    private final Long orderId;
    private final Long userId;
    private final String deliveryRequest;
    private final String address;
    private final String orderStatus;
    private final List<OrderDetailDto> orderDetails;

    public OrderResponseDto(Order order) {
        this.orderId = order.getOrderId();
        this.userId = order.getUser().getId();
        this.deliveryRequest = order.getDeliveryRequest();
        this.address = order.getAddress();
        this.orderStatus = order.getOrderStatus().name(); // OrderStatus를 문자열로 변환하여 할당

        Hibernate.initialize(order.getOrderDetails());

        // Null 체크 후에 처리
        this.orderDetails = Optional.ofNullable(order.getOrderDetails())
                .orElse(List.of()) // order.getOrderDetails()가 null인 경우 빈 리스트로 초기화
                .stream()
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