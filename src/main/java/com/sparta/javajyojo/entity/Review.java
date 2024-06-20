package com.sparta.javajyojo.entity;

import com.sparta.javajyojo.dto.ReviewRequestDto;
import com.sparta.javajyojo.dto.ReviewResponseDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "review")
public class Review extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order orderId;

    // Order객체의 userId값을 그냥 받아오면 될 듯 함
    private Long userId;

    private String review;

    private Long rating;

    public Review(ReviewRequestDto reviewRequestDto, Order order){
        this.orderId = order;
        this.userId = order.getUser().getId();
        this.review = reviewRequestDto.getReview();
        this.rating = reviewRequestDto.getRating();
    }

    public void update(ReviewRequestDto reviewRequestDto){
        this.review = reviewRequestDto.getReview();
    }

}
