package com.sparta.javajyojo.service;

import com.sparta.javajyojo.dto.ReviewRequestDto;
import com.sparta.javajyojo.dto.ReviewResponseDto;
import com.sparta.javajyojo.entity.Order;
import com.sparta.javajyojo.entity.Review;
import com.sparta.javajyojo.entity.User;
import com.sparta.javajyojo.enums.ErrorType;
import com.sparta.javajyojo.enums.OrderStatus;
import com.sparta.javajyojo.enums.UserRoleEnum;
import com.sparta.javajyojo.exception.CustomException;
import com.sparta.javajyojo.repository.OrderRepository;
import com.sparta.javajyojo.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    public final ReviewRepository reviewRepository;
    public final OrderRepository orderRepository;

    @Transactional
    public ReviewResponseDto createReview(User user, Long orderId, ReviewRequestDto requestDto) {

        Order order = findOrderById(orderId);
        if (user.getUserId() != order.getUser().getUserId()) {
            throw new CustomException(ErrorType.NO_AUTHENTICATION);
        }
        if (findReviewByOrderAndUserId(order, order.getUser().getUserId())) {
            throw new CustomException(ErrorType.DUPLICATE_Review_ID);
        }
        if (order.getOrderStatus() != OrderStatus.COMPLETED){
            throw new CustomException(ErrorType.INVALID_ORDER_STATUS);
        }

        Review review = new Review(requestDto, order);
        reviewRepository.save(review);

        return new ReviewResponseDto(review);
    }

    public Review getReviewById(Long reviewId){
        return reviewRepository.findById(reviewId).orElseThrow(
                () -> new CustomException(ErrorType.NOT_FOUND_REVIEW)
        );
    }

    @Transactional
    public ReviewResponseDto updateReview(User user, Long reviewId, ReviewRequestDto requestDto) {

        Review review = getReviewById(reviewId);
        if (user.getUserId() != review.getUserId()){
            System.out.println(user.getUserId());
            throw new CustomException(ErrorType.NO_AUTHENTICATION);
        }

        review.update(requestDto);

        return new ReviewResponseDto(review);
    }

    @Transactional
    public void deleteReview(User user, Long reviewId) {

        Review review = getReviewById(reviewId);
        if (user.getRole() != UserRoleEnum.ADMIN && user.getUserId() != review.getUserId()){
            throw new CustomException(ErrorType.NO_AUTHENTICATION);
        }

        reviewRepository.delete(review);
    }

    private Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(
                () -> new CustomException(ErrorType.NOT_FOUND_ORDER)
        );
    }

    private boolean findReviewByOrderAndUserId(Order order, Long userId){
        return reviewRepository.findReviewByOrderAndUserId(order, userId).isPresent();
    }

}
