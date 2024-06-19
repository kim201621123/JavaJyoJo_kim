package com.sparta.javajyojo.repository;

import com.sparta.javajyojo.entity.Order;
import com.sparta.javajyojo.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    List<OrderDetail> findAllByOrder(Order order);
}