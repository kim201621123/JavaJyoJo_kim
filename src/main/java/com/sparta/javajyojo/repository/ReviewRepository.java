package com.sparta.javajyojo.repository;

import com.sparta.javajyojo.entity.Order;
import com.sparta.javajyojo.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findReviewByOrderAndUserId(Order order, Long userId);

    Page<Review> findAllByUserId(Long userId, Pageable pageable);
}