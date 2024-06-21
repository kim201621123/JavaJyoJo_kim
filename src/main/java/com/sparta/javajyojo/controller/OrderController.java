package com.sparta.javajyojo.controller;

import com.sparta.javajyojo.dto.OrderRequestDto;
import com.sparta.javajyojo.dto.OrderResponseDto;
import com.sparta.javajyojo.entity.Order;
import com.sparta.javajyojo.enums.ErrorType;
import com.sparta.javajyojo.exception.CustomException;
import com.sparta.javajyojo.security.UserDetailsImpl;
import com.sparta.javajyojo.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                        @RequestBody OrderRequestDto orderRequestDto) {
        // 사용자 및 JWT 토큰 유효성 검사
        validateUser(userDetails);

        // 서비스에 주문 생성을 위임
        OrderResponseDto orderResponseDto = orderService.createOrder(orderRequestDto, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponseDto);
    }

    @GetMapping
    public ResponseEntity<Page<Order>> getOrders(@RequestParam int page, @RequestParam int size,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 사용자 및 JWT 토큰 유효성 검사
        validateUser(userDetails);

        // 서비스에 주문 목록 조회를 위임
        Page<Order> orders = orderService.getOrders(page, size);
        return ResponseEntity.ok().body(orders);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable Long orderId,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 사용자 및 JWT 토큰 유효성 검사
        validateUser(userDetails);

        // 서비스에 주문 조회를 위임
        Order order = orderService.getOrder(orderId, userDetails.getUser());
        return ResponseEntity.ok().body(order);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long orderId,
                                             @RequestBody OrderRequestDto orderRequestDto,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 사용자 및 JWT 토큰 유효성 검사
        validateUser(userDetails);

        // 서비스에 주문 수정을 위임
        Order updatedOrder = orderService.updateOrder(orderId, orderRequestDto, userDetails.getUser());
        return ResponseEntity.ok().body(updatedOrder);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long orderId,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 사용자 및 JWT 토큰 유효성 검사
        validateUser(userDetails);

        // 서비스에 주문 삭제를 위임
        orderService.deleteOrder(orderId, userDetails.getUser());
        return ResponseEntity.ok().body("주문 삭제가 완료되었습니다.");
    }

    private void validateUser(UserDetailsImpl userDetails) {
        if (userDetails == null) {
            throw new CustomException(ErrorType.UNAUTHORIZED_ACCESS);
        }
    }
}