package com.sparta.javajyojo.controller;

import com.sparta.javajyojo.dto.ReviewRequestDto;
import com.sparta.javajyojo.dto.ReviewResponseDto;
import com.sparta.javajyojo.entity.Order;
import com.sparta.javajyojo.entity.Review;
import com.sparta.javajyojo.entity.User;
import com.sparta.javajyojo.repository.OrderRepository;
import com.sparta.javajyojo.repository.ReviewRepository;
import com.sparta.javajyojo.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@Slf4j
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class ReviewController {

    public final ReviewService reviewService;
    public final ReviewRepository reviewRepository;
    public final OrderRepository orderRepository;

    @PostMapping("/{orderId}/reviews")
    public ResponseEntity<ReviewResponseDto> createReview(@RequestBody ReviewRequestDto reviewRequestDto,
                                                          @PathVariable Long orderId){
        Order order = reviewService.getOrderById(orderId);
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.createReview(reviewRequestDto, order));
    }

    @GetMapping("/reviews/{reviewId}")
    public ReviewResponseDto getReview(@PathVariable Long reviewId){
        return new ReviewResponseDto(reviewService.getReviewById(reviewId));
    }

    @PutMapping("/reviews/{reviewId}")
    public ReviewResponseDto updateReview(@RequestBody ReviewRequestDto reviewRequestDto,
                                          @PathVariable Long reviewId){ // 추후 userDetailsImpl로 받아오면 검증
        return reviewService.updateReview(reviewRequestDto, reviewId);
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ReviewResponseDto deleteReview(@PathVariable Long reviewId){
        return reviewService.deleteReview(reviewId);
    }


}
