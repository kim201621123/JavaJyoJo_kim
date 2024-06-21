package com.sparta.javajyojo.service;

import com.sparta.javajyojo.dto.OrderRequestDto;
import com.sparta.javajyojo.dto.OrderResponseDto;
import com.sparta.javajyojo.entity.Order;
import com.sparta.javajyojo.entity.OrderDetail;
import com.sparta.javajyojo.entity.User;
import com.sparta.javajyojo.enums.ErrorType;
import com.sparta.javajyojo.enums.OrderStatus;
import com.sparta.javajyojo.exception.CustomException;
import com.sparta.javajyojo.repository.MenuRepository;
import com.sparta.javajyojo.repository.OrderDetailRepository;
import com.sparta.javajyojo.repository.OrderRepository;
import com.sparta.javajyojo.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final UserRepository userRepository;

    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto, Long userId) {
        // 유저 정보 조회
        User user = findUserById(userId);

        // 총 주문 금액 계산
        int totalPrice = 0;
        for (OrderRequestDto.OrderDetailDto detailDto : orderRequestDto.getOrderDetails()) {
            // 메뉴 ID로 메뉴의 가격 조회
            int menuPrice = menuRepository.findById(detailDto.getMenuId())
                    .orElseThrow(() -> new CustomException(ErrorType.INVALID_MENU_ID))
                    .getPrice();
            // 메뉴 가격 * 수량을 총 주문 금액에 더함
            totalPrice += menuPrice * detailDto.getAmount();
        }

        // 주문 엔티티 생성 및 저장
        Order order = new Order();
        order.setAddress(orderRequestDto.getAddress());
        order.setDeliveryRequest(orderRequestDto.getDeliveryRequest());
        order.setOrderStatus(OrderStatus.NEW);
        order.setTotalPrice(totalPrice); // 총 주문 금액 저장
        order.setUser(user);

        orderRepository.save(order);

        // 주문 상세 엔티티 생성 및 저장
        for (OrderRequestDto.OrderDetailDto detailDto : orderRequestDto.getOrderDetails()) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setMenu(menuRepository.findById(detailDto.getMenuId())
                    .orElseThrow(() -> new CustomException(ErrorType.INVALID_MENU_ID)));
            orderDetail.setAmount(detailDto.getAmount());
            orderDetailRepository.save(orderDetail);
        }

        return new OrderResponseDto(order);
    }

    // 주문 목록 조회
    public Page<Order> getOrders(int page, int size) {
        return orderRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")));
    }

    // 주문 조회
    public Order getOrder(Long orderId) {
        return findOrderById(orderId);
    }

    @Transactional
    public Order updateOrder(Long orderId, OrderRequestDto orderRequestDto) {
        Order order = findOrderById(orderId);

        order.setDeliveryRequest(orderRequestDto.getDeliveryRequest());
        order.setAddress(orderRequestDto.getAddress());
        order.setOrderStatus(OrderStatus.UPDATED); // OrderStatus 열거형 사용

        return orderRepository.save(order);
    }

    @Transactional
    public void deleteOrder(Long orderId) {
        // 주문 ID로 주문 엔티티 조회
        Order order = findOrderById(orderId);

        // 주문 엔티티 삭제
        orderRepository.delete(order);
    }

    // 주문 ID로 주문 엔티티 조회 메서드
    private Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_ORDER));
    }

    // 사용자 ID로 사용자 엔티티 조회 메서드
    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_USER));
    }
}