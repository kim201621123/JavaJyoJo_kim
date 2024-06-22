package com.sparta.javajyojo.service;

import com.sparta.javajyojo.dto.ReviewRequestDto;
import com.sparta.javajyojo.dto.ReviewResponseDto;
import com.sparta.javajyojo.entity.Order;
import com.sparta.javajyojo.entity.Review;
import com.sparta.javajyojo.enums.ErrorType;
import com.sparta.javajyojo.exception.CustomException;
import com.sparta.javajyojo.repository.OrderRepository;
import com.sparta.javajyojo.repository.ReviewRepository;
import com.sparta.javajyojo.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    public final ReviewRepository reviewRepository;
    public final OrderRepository orderRepository;

    @Transactional
    public ReviewResponseDto createReview(UserDetailsImpl userDetails, ReviewRequestDto reviewRequestDto, Order order){

        if (userDetails.getUser().getId() != order.getUser().getId()){
            throw new CustomException(ErrorType.NO_AUTHENTICATION);
        }
        if (findReviewByOrderAndUserId(order, order.getUser().getId())){
            throw new CustomException(ErrorType.DUPLICATE_Review_ID);
        }
        Review review = new Review(reviewRequestDto, order);
        reviewRepository.save(review);
        return new ReviewResponseDto(review);
    }

    @Transactional
    public ReviewResponseDto updateReview(UserDetailsImpl userDetails, ReviewRequestDto reviewRequestDto,
                                          Long reviewId){
        Review review = getReviewById(reviewId);
        if (userDetails.getUser().getId() != review.getUserId()){
            System.out.println(userDetails.getUser().getId());
            throw new CustomException(ErrorType.NO_AUTHENTICATION);
        }
        review.update(reviewRequestDto);
        return new ReviewResponseDto(review);
    }

    @Transactional
    public ReviewResponseDto deleteReview(UserDetailsImpl userDetails, Long reviewId) {
        Review review = getReviewById(reviewId);
        System.out.println(userDetails.getUser().getId());
        System.out.println(review.getUserId());
        if (userDetails.getUser().getId() != review.getUserId()){
            throw new CustomException(ErrorType.NO_AUTHENTICATION);
        }
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
