package com.sparta.javajyojo.repository;

import com.sparta.javajyojo.entity.Order;
import com.sparta.javajyojo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByOrderId(Long orderId);
    Page<Order> findAllByUser(User user, Pageable pageable);
}