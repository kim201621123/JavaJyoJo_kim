package com.sparta.javajyojo.service;

import com.sparta.javajyojo.dto.ReviewRequestDto;
import com.sparta.javajyojo.dto.ReviewResponseDto;
import com.sparta.javajyojo.entity.Order;
import com.sparta.javajyojo.entity.Review;
import com.sparta.javajyojo.entity.User;
import com.sparta.javajyojo.enums.ErrorType;
import com.sparta.javajyojo.exception.CustomException;
import com.sparta.javajyojo.repository.OrderRepository;
import com.sparta.javajyojo.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    public final ReviewRepository reviewRepository;
    public final OrderRepository orderRepository;

    @Transactional
    public ReviewResponseDto createReview(ReviewRequestDto reviewRequestDto, Order order){

        if (findReviewByOrderAndUserId(order, order.getUser().getId())){
            throw new CustomException(ErrorType.DUPLICATE_Review_ID);
        }
        Review review = new Review(reviewRequestDto, order);
        reviewRepository.save(review);
        return new ReviewResponseDto(review);
    }

    @Transactional
    public ReviewResponseDto updateReview(ReviewRequestDto reviewRequestDto,
                                          Long reviewId){
        Review review = getReviewById(reviewId);
        review.update(reviewRequestDto);
        return new ReviewResponseDto(review);
    }

    @Transactional
    public ReviewResponseDto deleteReview(Long reviewId) {
        Review review = getReviewById(reviewId);
        ReviewResponseDto reviewResponseDto = new ReviewResponseDto(review);
        reviewRepository.delete(review);
        return reviewResponseDto;
    }


    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(
                () -> new CustomException(ErrorType.NOT_FOUND_ORDER));
    }

    public Review getReviewById(Long reviewId){
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_REVIEW));
    }
    public boolean findReviewByOrderAndUserId(Order order, Long userId){
        return reviewRepository.findReviewByOrderAndUserId(order, userId).isPresent();
    }

}
