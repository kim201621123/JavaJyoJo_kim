package com.sparta.javajyojo.user;

import static org.assertj.core.api.Assertions.*;

import com.sparta.javajyojo.dto.SignUpRequestDto;
import com.sparta.javajyojo.test.CommonTest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class SignUpRequestDtoTest {

    @DisplayName("회원 가입 DTO 생성")
    @Nested
    class createUserRequestDTO {
        @DisplayName("회원 가입 성공")
        @Test
        void createUserRequestDTO_success() {
            // given
            SignUpRequestDto requestDto = new SignUpRequestDto();
            requestDto.setUsername(CommonTest.TEST_USER_NAME);
            requestDto.setPassword(CommonTest.TEST_USER_PASSWORD);

            // when
            Set<ConstraintViolation<SignUpRequestDto>> violations = validate(requestDto);

            // then
            assertThat(violations).isEmpty();
        }

        @DisplayName("회원 가입 DTO 생성 실패 - 잘못된 username")
        @Test
        void createUserRequestDTO_fail_wrongUserName() {
            // given
            SignUpRequestDto requestDto = new SignUpRequestDto();
            requestDto.setUsername("Invalid user name"); // Invalid username pattern
            requestDto.setPassword(CommonTest.TEST_USER_PASSWORD);     // Invalid password pattern

            // when
            Set<ConstraintViolation<SignUpRequestDto>> violations = validate(requestDto);

            // then
            assertThat(violations).hasSize(1);
            assertThat(violations)
                    .extracting("message")
                    .contains("최소 4자 이상, 10자 이하이며 알파벳 소문자(a~z), 숫자(0~9)");
        }

        @DisplayName("회원 가입 DTO 생성 실패 - 잘못된 password")
        @Test
        void createUserRequestDTO_wrongPassword() {
            SignUpRequestDto requestDto = new SignUpRequestDto();
            requestDto.setUsername(CommonTest.TEST_USER_NAME); // Invalid username pattern
            requestDto.setPassword("Invalid password"); // Invalid password pattern

            // when
            Set<ConstraintViolation<SignUpRequestDto>> violations = validate(requestDto);

            // then
            assertThat(violations).hasSize(1);
            assertThat(violations)
                    .extracting("message")
                    .contains("최소 8자 이상, 15자 이하이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수문자");
        }
    }

    private Set<ConstraintViolation<SignUpRequestDto>> validate(SignUpRequestDto userRequestDTO) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        return validator.validate(userRequestDTO);
    }

}
