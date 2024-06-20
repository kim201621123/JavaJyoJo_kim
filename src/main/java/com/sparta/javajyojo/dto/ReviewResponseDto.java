package com.sparta.javajyojo.dto;

import com.sparta.javajyojo.entity.Order;
import com.sparta.javajyojo.entity.Review;
import lombok.Getter;


@Getter
public class ReviewResponseDto {
    private Long id;
    private Order orderId;
    private Long userId;
    private String review;
    private Long rating;

    public ReviewResponseDto(Review review) {
        this.id = review.getId();
        this.orderId = review.getOrderId();
        this.userId = review.getUserId();
        this.review = review.getReview();
        this.rating = review.getRating();
    }
}
