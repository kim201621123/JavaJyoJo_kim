package com.sparta.javajyojo.controller;

import com.sparta.javajyojo.dto.ReviewRequestDto;
import com.sparta.javajyojo.dto.ReviewResponseDto;
import com.sparta.javajyojo.entity.Order;
import com.sparta.javajyojo.repository.OrderRepository;
import com.sparta.javajyojo.repository.ReviewRepository;
import com.sparta.javajyojo.security.UserDetailsImpl;
import com.sparta.javajyojo.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class ReviewController {

    public final ReviewService reviewService;
    public final ReviewRepository reviewRepository;
    public final OrderRepository orderRepository;

    @PostMapping("/{orderId}/reviews")
    public ResponseEntity<ReviewResponseDto> createReview(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                          @RequestBody ReviewRequestDto reviewRequestDto,
                                                          @PathVariable Long orderId){
        Order order = reviewService.getOrderById(orderId);
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.createReview(userDetails, reviewRequestDto, order));
    }

    @GetMapping("/reviews/{reviewId}")
    public ResponseEntity<ReviewResponseDto> getReview(@PathVariable Long reviewId){
        return ResponseEntity.status(HttpStatus.OK).body(new ReviewResponseDto(reviewService.getReviewById(reviewId)));
    }

    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<ReviewResponseDto> updateReview(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                          @RequestBody ReviewRequestDto reviewRequestDto,
                                          @PathVariable Long reviewId){
        return ResponseEntity.status(HttpStatus.OK).body(reviewService.updateReview(userDetails, reviewRequestDto, reviewId));

    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<ReviewResponseDto> deleteReview(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                          @PathVariable Long reviewId){
        return ResponseEntity.status(HttpStatus.OK).body(reviewService.deleteReview(userDetails, reviewId));

    }


}
