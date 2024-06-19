package com.sparta.javajyojo;

import com.sparta.javajyojo.entity.Menu;
import com.sparta.javajyojo.repository.MenuRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final MenuRepository menuRepository;

    public DataInitializer(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // 메뉴 데이터가 없는 경우에만 초기 데이터를 삽입됨
        if (menuRepository.count() == 0) {
            menuRepository.save(new Menu("후라이드", 14900));
            menuRepository.save(new Menu("양념", 14900));
            menuRepository.save(new Menu("후라이드반/양념반", 14900));
            menuRepository.save(new Menu("떡볶이", 5000));
            menuRepository.save(new Menu("콜라", 2000));
            menuRepository.save(new Menu("사이다", 2000));
        }
    }
}