package com.sparta.javajyojo.repository;

import com.sparta.javajyojo.entity.Order;
import com.sparta.javajyojo.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByOrderByCreatedAtDesc();

    Optional<Review> findReviewByOrderAndUserId(Order order, Long userId);

}
