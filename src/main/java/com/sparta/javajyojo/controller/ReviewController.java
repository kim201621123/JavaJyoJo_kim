package com.sparta.javajyojo.controller;

import com.sparta.javajyojo.dto.ReviewRequestDto;
import com.sparta.javajyojo.dto.ReviewResponseDto;
import com.sparta.javajyojo.security.UserDetailsImpl;
import com.sparta.javajyojo.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/reviews")
    public ResponseEntity<ReviewResponseDto> createReview(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam Long orderId,
            @RequestBody ReviewRequestDto reviewRequestDto) {

        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.createReview(userDetails.getUser(), orderId, reviewRequestDto));
    }

    @GetMapping("/reviews/{reviewId}")
    public ResponseEntity<ReviewResponseDto> getReview(
            @PathVariable Long reviewId) {

        return ResponseEntity.ok().body(new ReviewResponseDto(reviewService.getReviewById(reviewId)));
    }

    @GetMapping("/reviews")
    public ResponseEntity<Page<ReviewResponseDto>> getReviews(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam int page,
            @RequestParam int size) {

        return ResponseEntity.ok().body(reviewService.getReviews(userDetails.getUser(), page, size));
    }


    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<ReviewResponseDto> updateReview(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long reviewId,
            @RequestBody ReviewRequestDto reviewRequestDto) {

        return ResponseEntity.ok().body(reviewService.updateReview(userDetails.getUser(), reviewId, reviewRequestDto));
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<String> deleteReview(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long reviewId) {

        reviewService.deleteReview(userDetails.getUser(), reviewId);

        return ResponseEntity.ok().body("리뷰가 삭제되었습니다.");
    }

}
