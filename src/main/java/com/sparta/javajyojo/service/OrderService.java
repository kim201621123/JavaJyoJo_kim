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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final UserRepository userRepository;

    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto, User user) {
        // 사용자가 주문을 생성할 권한이 있는지 검사 (필요시)
        // 필요한 경우 여기에서 주문 생성 권한을 확인할 수도 있음

        // 주문할 메뉴의 총 금액 계산
        int totalPrice = calculateTotalPrice(orderRequestDto);

        // 주문 엔티티 생성 및 저장
        Order order = new Order(user, orderRequestDto.getDeliveryRequest(), orderRequestDto.getAddress(), OrderStatus.NEW, totalPrice);
        orderRepository.save(order);

        // 주문 상세 엔티티 생성 및 저장
        saveOrderDetails(orderRequestDto, order);

        return new OrderResponseDto(order);
    }

    @Transactional
    public Order updateOrder(Long orderId, OrderRequestDto orderRequestDto, User user) {
        Order order = getOrder(orderId, user);

        // 사용자가 주문을 수정할 권한이 있는지 검사
        if (order.getUser().getId() != user.getId()) {
            throw new CustomException(ErrorType.UNAUTHORIZED_ACCESS);
        }

        order.setDeliveryRequest(orderRequestDto.getDeliveryRequest());
        order.setAddress(orderRequestDto.getAddress());
        order.setOrderStatus(OrderStatus.UPDATED);

        return orderRepository.save(order);
    }

    @Transactional
    public void deleteOrder(Long orderId, User user) {
        Order order = getOrder(orderId, user);

        // 사용자가 주문을 삭제할 권한이 있는지 검사
        if (order.getUser().getId() != user.getId()) {
            throw new CustomException(ErrorType.UNAUTHORIZED_ACCESS);
        }

        orderRepository.delete(order);
    }

    public Page<Order> getOrders(int page, int size) {
        return orderRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")));
    }

    public Order getOrder(Long orderId, User user) {
        Order order = orderRepository.findByOrderId(orderId);

        // 사용자가 조회하려는 주문의 사용자가 일치하는지 확인
        if (order.getUser().getId() != user.getId()) {
            throw new CustomException(ErrorType.UNAUTHORIZED_ACCESS);
        }

        return order;
    }

    private int calculateTotalPrice(OrderRequestDto orderRequestDto) {
        int totalPrice = 0;
        for (OrderRequestDto.OrderDetailDto detailDto : orderRequestDto.getOrderDetails()) {
            int menuPrice = menuRepository.findById(detailDto.getMenuId())
                    .orElseThrow(() -> new CustomException(ErrorType.INVALID_INPUT))
                    .getPrice();
            totalPrice += menuPrice * detailDto.getAmount();
        }
        return totalPrice;
    }

    private void saveOrderDetails(OrderRequestDto orderRequestDto, Order order) {
        for (OrderRequestDto.OrderDetailDto detailDto : orderRequestDto.getOrderDetails()) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setMenu(menuRepository.findById(detailDto.getMenuId())
                    .orElseThrow(() -> new CustomException(ErrorType.INVALID_INPUT)));
            orderDetail.setAmount(detailDto.getAmount());
            orderDetailRepository.save(orderDetail);
        }
    }
}