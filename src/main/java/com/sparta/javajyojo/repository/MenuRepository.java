package com.sparta.javajyojo.repository;

import com.sparta.javajyojo.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    Menu findByMenuId(Long menuId);
}