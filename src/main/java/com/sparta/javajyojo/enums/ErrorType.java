package com.sparta.javajyojo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType {
    // LOGIN
    INVALID_ACCOUNT_ID(HttpStatus.UNAUTHORIZED, "아이디가 일치하지 않습니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),

    // USER
    DUPLICATE_ACCOUNT_ID(HttpStatus.LOCKED, "이미 아이디가 존재합니다."),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."),
    PASSWORD_SAME(HttpStatus.UNAUTHORIZED, "현재 비밀번호와 동일한 비밀번호로 변경할 수 없습니다."),
    PASSWORD_RECENTLY_USED(HttpStatus.LOCKED, "최근에 사용한 비밀번호는 사용할 수 없습니다."),

    // REVIEW
    NOT_FOUND_REVIEW(HttpStatus.NOT_FOUND, "존재하지 않는 리뷰입니다."),
    NO_AUTHENTICATION(HttpStatus.UNAUTHORIZED, "권한이 없습니다."),

    // JWT
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 리프레시 토큰입니다. 다시 로그인 해주세요."),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다. 다시 로그인 해주세요."),

    REQUIRES_LOGIN(HttpStatus.LOCKED, "로그인이 필요한 서비스입니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
