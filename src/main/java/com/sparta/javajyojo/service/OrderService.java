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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final UserRepository userRepository;

    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto, User user) {
        int totalPrice = calculateTotalPrice(orderRequestDto);
        Order order = new Order(user, orderRequestDto.getDeliveryRequest(), orderRequestDto.getAddress(), OrderStatus.NEW, totalPrice);
        orderRepository.save(order);
        List<OrderDetail> orderDetails = saveOrderDetails(orderRequestDto, order);
        return new OrderResponseDto(order, orderDetails);  // 주문 상세 정보와 총 금액을 포함한 응답 반환
    }

    @Transactional
    public OrderResponseDto updateOrder(Long orderId, OrderRequestDto orderRequestDto, User user) {
        Order order = getOrderEntity(orderId, user);
        if (order.getUser().getId() != user.getId()) {
            throw new CustomException(ErrorType.UNAUTHORIZED_ACCESS);
        }
        order.setDeliveryRequest(orderRequestDto.getDeliveryRequest());
        order.setAddress(orderRequestDto.getAddress());
        order.setOrderStatus(OrderStatus.UPDATED);

        // 주문 상세 정보가 업데이트 될 경우 총 금액 다시 계산
        int totalPrice = calculateTotalPrice(orderRequestDto);
        order.setTotalPrice(totalPrice);

        orderRepository.save(order);
        List<OrderDetail> orderDetails = saveOrderDetails(orderRequestDto, order);
        return new OrderResponseDto(order, orderDetails);
    }

    @Transactional
    public void deleteOrder(Long orderId, User user) {
        Order order = getOrderEntity(orderId, user);
        if (order.getUser().getId() != user.getId()) {
            throw new CustomException(ErrorType.UNAUTHORIZED_ACCESS);
        }
        orderRepository.delete(order);
    }

    public Page<OrderResponseDto> getOrders(int page, int size, User user) {
        Page<Order> orders = orderRepository.findAllByUser(user, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")));
        return orders.map(order -> new OrderResponseDto(order, order.getOrderDetails()));
    }

    public OrderResponseDto getOrder(Long orderId, User user) {
        Order order = getOrderEntity(orderId, user);
        return new OrderResponseDto(order, order.getOrderDetails());
    }

    private Order getOrderEntity(Long orderId, User user) {
        Order order = orderRepository.findByOrderId(orderId);
        if (order == null || order.getUser().getId() != user.getId()) {
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

    private List<OrderDetail> saveOrderDetails(OrderRequestDto orderRequestDto, Order order) {
        List<OrderDetail> orderDetails = orderRequestDto.getOrderDetails().stream().map(detailDto -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setMenu(menuRepository.findById(detailDto.getMenuId())
                    .orElseThrow(() -> new CustomException(ErrorType.INVALID_INPUT)));
            orderDetail.setAmount(detailDto.getAmount());
            return orderDetailRepository.save(orderDetail);
        }).collect(Collectors.toList());
        return orderDetails;
    }
}