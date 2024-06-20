package com.sparta.javajyojo.service;

import com.sparta.javajyojo.dto.OrderRequestDto;
import com.sparta.javajyojo.dto.OrderResponseDto;
import com.sparta.javajyojo.entity.Order;
import com.sparta.javajyojo.entity.User;
import com.sparta.javajyojo.enums.OrderStatus;
import com.sparta.javajyojo.repository.MenuRepository;
import com.sparta.javajyojo.repository.OrderDetailRepository;
import com.sparta.javajyojo.repository.OrderRepository;
import com.sparta.javajyojo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final UserRepository userRepository;  // 사용자 정보를 조회할 UserRepository


    // 주문 생성
    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        Order order = new Order();
        order.setAddress(orderRequestDto.getAddress());
        order.setDeliveryRequest(orderRequestDto.getDeliveryRequest());
        order.setOrderStatus(OrderStatus.NEW);
        order.setUser(user); // 유저 정보를 설정

        orderRepository.save(order);

        return new OrderResponseDto(order);
    }

    // 주문 목록 조회
    public Page<Order> getOrders(int page, int size) {
        return orderRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")));
    }

    // 주문 조회
    public Order getOrder(Long orderId) {
        return orderRepository.findByOrderId(orderId);
    }

    // 주문 수정
    public Order updateOrder(Long orderId, OrderRequestDto orderRequestDto) {
        Order order = getOrder(orderId);

        order.setDeliveryRequest(orderRequestDto.getDeliveryRequest());
        order.setAddress(orderRequestDto.getAddress());
        order.setOrderStatus(OrderStatus.UPDATED); // OrderStatus 열거형 사용

        return orderRepository.save(order);
    }


    public void deleteOrder(Long orderId) {
        // 주문 ID로 주문 엔티티 조회
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다. ID: " + orderId));

        // 주문 엔티티 삭제
        orderRepository.delete(order);
    }

}