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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final OrderDetailRepository orderDetailRepository;

    @Transactional
    public OrderResponseDto createOrder(User user, OrderRequestDto requestDto) {

        int totalPrice = calculateTotalPrice(requestDto);
        Order order = new Order(user, requestDto.getDeliveryRequest(), requestDto.getAddress(), OrderStatus.NEW, totalPrice);

        orderRepository.save(order);
        List<OrderDetail> orderDetails = saveOrderDetails(requestDto, order);
        return new OrderResponseDto(order, orderDetails);  // 주문 상세 정보와 총 금액을 포함한 응답 반환
    }

    public Page<OrderResponseDto> getOrders(User user, int page, int size) {
        Page<Order> orders = orderRepository.findAllByUser(user, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")));
        return orders.map(order -> new OrderResponseDto(order, order.getOrderDetails()));
    }

    public OrderResponseDto getOrder(User user, Long orderId) {
        Order order = getOrderEntity(user, orderId);
        return new OrderResponseDto(order, order.getOrderDetails());
    }

    @Transactional
    public OrderResponseDto updateOrder(User user, Long orderId, OrderRequestDto orderRequestDto) {

        Order order = getOrderEntity(user, orderId);
        if (order.getUser().getUserId() != user.getUserId()) {
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
    public void deleteOrder(User user, Long orderId) {

        Order order = getOrderEntity(user, orderId);
        if (order.getUser().getUserId() != user.getUserId()) {
            throw new CustomException(ErrorType.UNAUTHORIZED_ACCESS);
        }

        orderRepository.delete(order);
    }

    private Order getOrderEntity(User user, Long orderId) {

        Order order = orderRepository.findByOrderId(orderId);
        if (order == null || order.getUser().getUserId() != user.getUserId()) {
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
            orderDetail.setMenu(menuRepository.findById(detailDto.getMenuId()).orElseThrow(
                    () -> new CustomException(ErrorType.INVALID_INPUT)
            ));
            orderDetail.setAmount(detailDto.getAmount());

            return orderDetailRepository.save(orderDetail);
        }).collect(Collectors.toList());

        return orderDetails;
    }

}
