package com.sparta.javajyojo.repository;

import com.sparta.javajyojo.entity.PasswordHistory;
import com.sparta.javajyojo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PasswordHistoryRepository extends JpaRepository<PasswordHistory, Long> {
    List<PasswordHistory> findTop3ByUserOrderByChangeDateDesc(User user);
}
