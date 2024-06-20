package com.sparta.javajyojo.dto;

import com.sparta.javajyojo.entity.Order;
import com.sparta.javajyojo.entity.Review;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ReviewResponseDto {
    private Long id;
    private Long orderId;
    private Long userId;
    private String review;
    private Long rating;

    public ReviewResponseDto(Review review) {
        this.id = review.getReviewId();
        this.orderId = review.getOrder().getOrderId(); //쿼리는 2번 날라가게 됨
        this.userId = review.getUserId();
        this.review = review.getReview();
        this.rating = review.getRating();
    }
}
