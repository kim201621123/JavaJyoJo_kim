package com.sparta.javajyojo.service;

import com.sparta.javajyojo.dto.ReviewRequestDto;
import com.sparta.javajyojo.dto.ReviewResponseDto;
import com.sparta.javajyojo.entity.Order;
import com.sparta.javajyojo.entity.Review;
import com.sparta.javajyojo.entity.User;
import com.sparta.javajyojo.repository.OrderRepository;
import com.sparta.javajyojo.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    public final ReviewRepository reviewRepository;
    public final OrderRepository orderRepository;

    public ReviewResponseDto createReview(ReviewRequestDto reviewRequestDto, Order order){
        Review review = new Review(reviewRequestDto, order);
        reviewRepository.save(review);

        return new ReviewResponseDto(review);
    }

    @Transactional
    public ReviewResponseDto updateReview(ReviewRequestDto reviewRequestDto,
                                          Review review){
        review.update(reviewRequestDto);
        return new ReviewResponseDto(review);
    }

    public ReviewResponseDto deleteReview(Long reviewId) {
        Review review = getReviewById(reviewId);
        ReviewResponseDto reviewResponseDto = new ReviewResponseDto(review);
        reviewRepository.delete(review);
        return reviewResponseDto;
    }


    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with id " + id));
    }

    public Review getReviewById(Long id){
        return reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("review not found with id " + id));
    }



    public List<ReviewResponseDto> getAllReviews() {
        return reviewRepository.findAllByOrderByCreatedAtDesc()
                .stream().map(ReviewResponseDto::new).toList();
    }


}
