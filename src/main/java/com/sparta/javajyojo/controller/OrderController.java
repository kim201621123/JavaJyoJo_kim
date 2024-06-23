package com.sparta.javajyojo.controller;

import com.sparta.javajyojo.dto.OrderRequestDto;
import com.sparta.javajyojo.dto.OrderResponseDto;
import com.sparta.javajyojo.enums.ErrorType;
import com.sparta.javajyojo.exception.CustomException;
import com.sparta.javajyojo.security.UserDetailsImpl;
import com.sparta.javajyojo.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Slf4j
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody OrderRequestDto orderRequestDto) {

        validateUser(userDetails);

        if (orderRequestDto.getOrderDetails() == null) {
            orderRequestDto.setOrderDetails(new ArrayList<>());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(userDetails.getUser(), orderRequestDto));
    }

    @GetMapping
    public ResponseEntity<Page<OrderResponseDto>> getOrders(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam int page,
            @RequestParam int size) {

        validateUser(userDetails);

        return ResponseEntity.ok().body(orderService.getOrders(userDetails.getUser(), page, size));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrder(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long orderId) {

        validateUser(userDetails);

        return ResponseEntity.ok().body(orderService.getOrder(userDetails.getUser(), orderId));
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> updateOrder(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long orderId,
            @RequestBody OrderRequestDto orderRequestDto) {

        validateUser(userDetails);

        if (orderRequestDto.getOrderDetails() == null) {
            orderRequestDto.setOrderDetails(new ArrayList<>());
        }

        return ResponseEntity.ok().body(orderService.updateOrder(userDetails.getUser(), orderId, orderRequestDto));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> deleteOrder(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long orderId) {

        validateUser(userDetails);
        orderService.deleteOrder(userDetails.getUser(), orderId);

        return ResponseEntity.ok().body("주문이 삭제되었습니다.");
    }

    private void validateUser(UserDetailsImpl userDetails) {
        if (userDetails == null || userDetails.getUser() == null) {
            throw new CustomException(ErrorType.UNAUTHORIZED_ACCESS);
        }
    }
}