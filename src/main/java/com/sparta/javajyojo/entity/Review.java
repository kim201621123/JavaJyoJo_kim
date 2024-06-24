package com.sparta.javajyojo.entity;

import com.sparta.javajyojo.dto.ReviewRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "reviews")
@NoArgsConstructor
public class Review extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private Long userId;

    private String review;

    private Long rating;

    public Review(ReviewRequestDto reviewRequestDto, Order order) {
        this.order = order;
        this.userId = order.getUser().getUserId();
        this.review = reviewRequestDto.getReview();
        this.rating = reviewRequestDto.getRating();
    }

    public Review(Order order, Long userId, String review, Long rating) {
        this.order = order;
        this.userId = userId;
        this.review = review;
        this.rating = rating;
    }

    public void update(ReviewRequestDto reviewRequestDto) {
        this.review = reviewRequestDto.getReview();
        this.rating = reviewRequestDto.getRating();
    }

}
