package com.sparta.javajyojo.service;

import com.sparta.javajyojo.dto.ProfileRequestDto;
import com.sparta.javajyojo.dto.ProfileResponseDto;
import com.sparta.javajyojo.dto.SignUpRequestDto;
import com.sparta.javajyojo.entity.PasswordHistory;
import com.sparta.javajyojo.entity.User;
import com.sparta.javajyojo.enums.UserRoleEnum;
import com.sparta.javajyojo.enums.ErrorType;
import com.sparta.javajyojo.exception.CustomException;
import com.sparta.javajyojo.repository.PasswordHistoryRepository;
import com.sparta.javajyojo.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordHistoryRepository passwordHistoryRepository;
    private final PasswordEncoder passwordEncoder;

    // ADMIN_TOKEN
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    public ProfileResponseDto signUp(SignUpRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isPresent()) {
            throw new CustomException(ErrorType.DUPLICATE_ACCOUNT_ID);
        }

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new CustomException(ErrorType.INVALID_ADMIN_PASSWORD);
            }
            role = UserRoleEnum.ADMIN;
        }

        User user = new User(
            username,
            password,
            requestDto.getName(),
            requestDto.getIntro(),
            role
        );
        userRepository.save(user);

        return new ProfileResponseDto(user);
    }

    @Transactional
    public void signOut(Long userId) {
        User user = findById(userId);
        user.signOut();
    }

    @Transactional
    public void logOut(Long userId) {
        User user = findById(userId);
        user.logOut();
    }

    public ProfileResponseDto getProfile(Long userId) {
        return new ProfileResponseDto(findById(userId));
    }

    @Transactional
    public ProfileResponseDto update(Long userId, ProfileRequestDto requestDto) {

        User user = findById(userId);
        String newEncodePassword = null;

        // 비밀번호 수정 시
        if (requestDto.getPassword() != null) {
            // 본인 확인을 위해 현재 비밀번호를 입력하여 올바른 경우
            if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
                throw new CustomException(ErrorType.INVALID_PASSWORD);
            }
            // 현재 비밀번호와 동일한 비밀번호로는 변경할 수 없음
            if (passwordEncoder.matches(requestDto.getPassword(), requestDto.getNewPassword())) {
                throw new CustomException(ErrorType.PASSWORD_SAME);
            }
            // 최근 3번 안에 사용한 비밀번호는 사용할 수 없도록 제한
            List<PasswordHistory> recentPasswords = passwordHistoryRepository.findTop3ByUserOrderByChangeDateDesc(user);
            boolean isInPreviousPasswords = recentPasswords.stream()
                .anyMatch(pw -> passwordEncoder.matches(requestDto.getNewPassword(), String.valueOf(pw)));
            if (isInPreviousPasswords) {
                throw new CustomException(ErrorType.PASSWORD_RECENTLY_USED);
            }

            newEncodePassword = passwordEncoder.encode(requestDto.getNewPassword());

            PasswordHistory passwordHistory = new PasswordHistory(user, newEncodePassword);
            passwordHistoryRepository.save(passwordHistory);
        }

        user.update(
            Optional.ofNullable(newEncodePassword),
            Optional.ofNullable(requestDto.getName()),
            Optional.ofNullable(requestDto.getIntro())
        );

        return new ProfileResponseDto(user);
    }

    private User findById(Long id) {
        return userRepository.findById(id).orElseThrow(
            () -> new CustomException(ErrorType.NOT_FOUND_USER)
        );
    }

}
