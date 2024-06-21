package com.sparta.javajyojo.controller;

import com.sparta.javajyojo.dto.OrderRequestDto;
import com.sparta.javajyojo.dto.OrderResponseDto;
import com.sparta.javajyojo.entity.Order;
import com.sparta.javajyojo.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // 주문 생성
    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody OrderRequestDto orderRequestDto, @RequestParam Long userId) {
        OrderResponseDto orderResponseDto = orderService.createOrder(orderRequestDto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponseDto);
    }

    // 주문 목록 조회
    @GetMapping
    public ResponseEntity<Page<Order>> getOrders(@RequestParam int page, @RequestParam int size) {
        Page<Order> orders = orderService.getOrders(page, size);
        return ResponseEntity.ok().body(orders);
    }

    // 주문 조회
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable Long orderId) {
        Order order = orderService.getOrder(orderId);
        return ResponseEntity.ok().body(order);
    }

    // 주문 수정
    @PutMapping("/{orderId}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long orderId, @RequestBody OrderRequestDto orderRequestDto) {
        Order updatedOrder = orderService.updateOrder(orderId, orderRequestDto);
        return ResponseEntity.ok().body(updatedOrder);
    }

    // 주문 삭제
    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.ok().body("주문 삭제가 완료되었습니다.");
    }
}