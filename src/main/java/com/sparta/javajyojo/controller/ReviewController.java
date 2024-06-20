package com.sparta.javajyojo.controller;

import com.sparta.javajyojo.dto.ReviewRequestDto;
import com.sparta.javajyojo.dto.ReviewResponseDto;
import com.sparta.javajyojo.entity.Order;
import com.sparta.javajyojo.repository.OrderRepository;
import com.sparta.javajyojo.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReviewController {

    public final ReviewService reviewService;
    public final OrderRepository orderRepository;

    @PostMapping("/review")
    public ReviewResponseDto createReview(@RequestBody ReviewRequestDto reviewRequestDto,
                                          @PathVariable Long orderId){
        Order order = reviewService.getOrderById(orderId);
        return reviewService.createReview(reviewRequestDto, order);
    }

}
