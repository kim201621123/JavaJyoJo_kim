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
        return new ResponseEntity<>(orderResponseDto, HttpStatus.CREATED);
    }

    // 주문 목록 조회
    @GetMapping
    public Page<Order> getOrders(@RequestParam int page, @RequestParam int size) {
        return orderService.getOrders(page, size);
    }

    // 주문 조회
    @GetMapping("/{orderId}")
    public Order getOrder(@PathVariable Long orderId) {
        return orderService.getOrder(orderId);
    }

    // 주문 수정
    @PutMapping("/{orderId}")
    public Order updateOrder(@PathVariable Long orderId, @RequestBody OrderRequestDto orderRequestDto) {
        return orderService.updateOrder(orderId, orderRequestDto);
    }

    // 주문 삭제
    @DeleteMapping("/{orderId}")
    public void deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
    }
}