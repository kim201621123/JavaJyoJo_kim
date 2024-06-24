package com.sparta.javajyojo;

import com.sparta.javajyojo.entity.Menu;
import com.sparta.javajyojo.entity.Order;
import com.sparta.javajyojo.entity.Review;
import com.sparta.javajyojo.entity.User;
import com.sparta.javajyojo.enums.UserRoleEnum;
import com.sparta.javajyojo.repository.MenuRepository;
import com.sparta.javajyojo.repository.OrderRepository;
import com.sparta.javajyojo.repository.ReviewRepository;
import com.sparta.javajyojo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final MenuRepository menuRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final OrderRepository orderRepository;
    private final ReviewRepository reviewRepository;

    public DataInitializer(MenuRepository menuRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, OrderRepository orderRepository, ReviewRepository reviewRepository) {
        this.menuRepository = menuRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.orderRepository = orderRepository;
        this.reviewRepository = reviewRepository;
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

        if (userRepository.findAll().isEmpty()) {
            userRepository.save(new User(
                    "admin",
                    passwordEncoder.encode("A1234qwer!"),
                    "손호찬",
                    "한마디!",
                    UserRoleEnum.valueOf("ADMIN")
            ));
            userRepository.save(new User(
                    "equis3351",
                    passwordEncoder.encode("A1234qwer!"),
                    "남현",
                    "두마디!",
                    UserRoleEnum.valueOf("WITHOUT")
            ));
            userRepository.save(new User(
                    "gaeun7",
                    passwordEncoder.encode("A1234qwer!"),
                    "김가은",
                    "세마디!",
                    UserRoleEnum.valueOf("USER")
            ));
            userRepository.save(new User(
                    "kim2016",
                    passwordEncoder.encode("A1234qwer!"),
                    "김현성",
                    "네마디!",
                    UserRoleEnum.valueOf("USER")
            ));
            userRepository.save(new User(
                    "Luel1197",
                    passwordEncoder.encode("A1234qwer!"),
                    "손아엘",
                    "다섯마디!",
                    UserRoleEnum.valueOf("USER")
            ));
        }

//        if (orderRepository.findAll().isEmpty()) {
//            orderRepository.save(new Order());
//        }
//
//        if (reviewRepository.findAll().isEmpty()) {
//            reviewRepository.save(new Review());
//        }
    }
}