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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    public final ReviewService reviewService;
    public final ReviewRepository reviewRepository;
    public final OrderRepository orderRepository;

    @PostMapping("/{orderId}")
    public ReviewResponseDto createReview(@RequestBody ReviewRequestDto reviewRequestDto,
                                          @PathVariable Long orderId){
        Order order = reviewService.getOrderById(orderId);
        return reviewService.createReview(reviewRequestDto, order);
    }

    @GetMapping("/getAllReviews")
    public List<ReviewResponseDto> getAllReviews() {
        return reviewService.getAllReviews();
    }

    @PutMapping("/{reviewId}")
    public ReviewResponseDto updateReview(@RequestBody ReviewRequestDto reviewRequestDto,
                                          @PathVariable Long reviewId){ //추후 userDetailsImpl로 받아오면 검증
        Review review = reviewService.getReviewById(reviewId);
        return reviewService.updateReview(reviewRequestDto, review);
    }


}
