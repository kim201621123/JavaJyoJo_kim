package com.sparta.javajyojo;

import com.sparta.javajyojo.dto.ReviewRequestDto;
import com.sparta.javajyojo.entity.*;
import com.sparta.javajyojo.enums.OrderStatus;
import com.sparta.javajyojo.enums.UserRoleEnum;
import com.sparta.javajyojo.repository.*;
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
    private final OrderDetailRepository orderDetailRepository;
    private final PasswordHistoryRepository passwordHistoryRepository;

    public DataInitializer(MenuRepository menuRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, OrderRepository orderRepository, ReviewRepository reviewRepository, OrderDetailRepository orderDetailRepository, PasswordHistoryRepository passwordHistoryRepository) {
        this.menuRepository = menuRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.orderRepository = orderRepository;
        this.reviewRepository = reviewRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.passwordHistoryRepository = passwordHistoryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // 메뉴 데이터가 없는 경우에만 초기 데이터를 삽입됨
        if (menuRepository.findAll().isEmpty()) {
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

            if (orderRepository.findAll().isEmpty()) {
                orderRepository.save(new Order(
                        userRepository.findByUsername("gaeun7").get(),
                        "문 앞에 놔주세요",
                        "서울시 영등포구 여의도동 123-45",
                        OrderStatus.NEW,
                        14900
                        ));
                orderRepository.save(new Order(
                        userRepository.findByUsername("gaeun7").get(),
                        "문 앞에 놔주시오",
                        "서울시 영등포구 여의도동 123-45",
                        OrderStatus.COMPLETED,
                        14900 * 2
                ));
                orderRepository.save(new Order(
                        userRepository.findByUsername("kim2016").get(),
                        "문 앞에 놓아 주실래요?",
                        "부산시 해운대구 우동 123-45",
                        OrderStatus.UPDATED,
                        14900
                ));
                orderRepository.save(new Order(
                        userRepository.findByUsername("kim2016").get(),
                        "문 앞에 두고 가",
                        "부산시 해운대구 우동 123-45",
                        OrderStatus.COMPLETED,
                        14900 + 2000 + 2000
                ));
                orderRepository.save(new Order(
                        userRepository.findByUsername("Luel1197").get(),
                        "문 앞에 두고 가주세요",
                        "충남 보령시 신흑동 123-45",
                        OrderStatus.COMPLETED,
                        (14900 + 5000 + 2000) * 2
                ));
                orderRepository.save(new Order(
                        userRepository.findByUsername("Luel1197").get(),
                        "문 앞에 두세요",
                        "충남 보령시 신흑동 123-45",
                        OrderStatus.CANCELLED,
                        2000
                ));
            }

            if (orderDetailRepository.findAll().isEmpty()) {
                orderDetailRepository.save(new OrderDetail(
                        orderRepository.findByOrderId(1L),
                        menuRepository.findById(1L).get(),
                        1
                ));
                orderDetailRepository.save(new OrderDetail(
                        orderRepository.findByOrderId(2L),
                        menuRepository.findById(2L).get(),
                        2
                ));
                orderDetailRepository.save(new OrderDetail(
                        orderRepository.findByOrderId(3L),
                        menuRepository.findById(3L).get(),
                        1
                ));
                orderDetailRepository.save(new OrderDetail(
                        orderRepository.findByOrderId(4L),
                        menuRepository.findById(1L).get(),
                        1
                ));
                orderDetailRepository.save(new OrderDetail(
                        orderRepository.findByOrderId(4L),
                        menuRepository.findById(5L).get(),
                        1
                ));
                orderDetailRepository.save(new OrderDetail(
                        orderRepository.findByOrderId(4L),
                        menuRepository.findById(6L).get(),
                        1
                ));
                orderDetailRepository.save(new OrderDetail(
                        orderRepository.findByOrderId(5L),
                        menuRepository.findById(2L).get(),
                        2
                ));
                orderDetailRepository.save(new OrderDetail(
                        orderRepository.findByOrderId(5L),
                        menuRepository.findById(4L).get(),
                        2
                ));
                orderDetailRepository.save(new OrderDetail(
                        orderRepository.findByOrderId(5L),
                        menuRepository.findById(6L).get(),
                        2
                ));
                orderDetailRepository.save(new OrderDetail(
                        orderRepository.findByOrderId(6L),
                        menuRepository.findById(6L).get(),
                        1
                ));
            }

            if (reviewRepository.findAll().isEmpty()) {
                ReviewRequestDto requestDto = new ReviewRequestDto();
                requestDto.setReview("review Test1");
                requestDto.setRating(5L);
                reviewRepository.save(new Review(
                        requestDto,
                        orderRepository.findByOrderId(2L)
                ));
                requestDto.setReview("review Test2");
                requestDto.setRating(5L);
                reviewRepository.save(new Review(
                        requestDto,
                        orderRepository.findByOrderId(4L)
                ));
                requestDto.setReview("review Test3");
                requestDto.setRating(4L);
                reviewRepository.save(new Review(
                        requestDto,
                        orderRepository.findByOrderId(5L)
                ));
            }

            if (passwordHistoryRepository.findAll().isEmpty()) {
                passwordHistoryRepository.save(new PasswordHistory(
                        userRepository.findById(3L).get(),
                        passwordEncoder.encode("Bqwer1234!")
                ));
                passwordHistoryRepository.save(new PasswordHistory(
                        userRepository.findById(3L).get(),
                        passwordEncoder.encode("Cqwer1234!")
                ));
                passwordHistoryRepository.save(new PasswordHistory(
                        userRepository.findById(3L).get(),
                        passwordEncoder.encode("Dqwer1234!")
                ));
                passwordHistoryRepository.save(new PasswordHistory(
                        userRepository.findById(3L).get(),
                        passwordEncoder.encode("Eqwer1234!")
                ));
                passwordHistoryRepository.save(new PasswordHistory(
                        userRepository.findById(4L).get(),
                        passwordEncoder.encode("Eqwer1234!")
                ));
            }
        }

    }
}