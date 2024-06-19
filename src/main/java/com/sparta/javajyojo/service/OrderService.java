package com.sparta.javajyojo.service;

import com.sparta.javajyojo.dto.OrderRequestDto;
import com.sparta.javajyojo.entity.Menu;
import com.sparta.javajyojo.entity.Order;
import com.sparta.javajyojo.entity.OrderDetail;
import com.sparta.javajyojo.repository.MenuRepository;
import com.sparta.javajyojo.repository.OrderDetailRepository;
import com.sparta.javajyojo.repository.OrderRepository;
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


    // 주문 생성
    public Order createOrder(OrderRequestDto orderRequestDto) {
        Order order = new Order();
        order.setDeliveryRequest(orderRequestDto.getDeliveryRequest());
        order.setAddress(orderRequestDto.getAddress());
        order.setOrderStatus("ORDERED");

        Order savedOrder = orderRepository.save(order);

        for (OrderRequestDto.OrderDetailDto detailDto : orderRequestDto.getOrderDetails()) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(savedOrder);
            orderDetail.setAmount(detailDto.getAmount());
            Menu menu = menuRepository.findByMenuId(detailDto.getMenuId());
            orderDetail.setMenu(menu);
            orderDetailRepository.save(orderDetail);
        }
        return savedOrder;
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
        order.setOrderStatus("UPDATED");
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