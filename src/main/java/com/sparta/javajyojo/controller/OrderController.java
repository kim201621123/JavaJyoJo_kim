package com.sparta.javajyojo.controller;

import com.sparta.javajyojo.dto.OrderRequestDto;
import com.sparta.javajyojo.dto.OrderResponseDto;
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
        validateUser(userDetails);
        OrderResponseDto orderResponseDto = orderService.createOrder(orderRequestDto, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponseDto);
    }

    @GetMapping
    public ResponseEntity<Page<OrderResponseDto>> getOrders(@RequestParam int page, @RequestParam int size,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        validateUser(userDetails);
        Page<OrderResponseDto> orders = orderService.getOrders(page, size, userDetails.getUser());
        return ResponseEntity.ok().body(orders);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrder(@PathVariable Long orderId,
                                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {
        validateUser(userDetails);
        OrderResponseDto order = orderService.getOrder(orderId, userDetails.getUser());
        return ResponseEntity.ok().body(order);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> updateOrder(@PathVariable Long orderId,
                                                        @RequestBody OrderRequestDto orderRequestDto,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        validateUser(userDetails);
        OrderResponseDto updatedOrder = orderService.updateOrder(orderId, orderRequestDto, userDetails.getUser());
        return ResponseEntity.ok().body(updatedOrder);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long orderId,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails) {
        validateUser(userDetails);
        orderService.deleteOrder(orderId, userDetails.getUser());
        return ResponseEntity.ok("주문이 삭제되었습니다.");
    }

    private void validateUser(UserDetailsImpl userDetails) {
        if (userDetails == null || userDetails.getUser() == null) {
            throw new CustomException(ErrorType.UNAUTHORIZED_ACCESS);
        }
    }
}