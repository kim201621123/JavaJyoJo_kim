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

    // SIGNUP
    INVALID_ADMIN_PASSWORD(HttpStatus.FORBIDDEN, "관리자 암호가 틀려 등록이 불가능합니다."),

    // USER
    DUPLICATE_ACCOUNT_ID(HttpStatus.LOCKED, "이미 아이디가 존재합니다."),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."),
    PASSWORD_SAME(HttpStatus.UNAUTHORIZED, "현재 비밀번호와 동일한 비밀번호로 변경할 수 없습니다."),
    PASSWORD_RECENTLY_USED(HttpStatus.LOCKED, "최근에 사용한 비밀번호는 사용할 수 없습니다."),

    // ORDER
    INVALID_MENU_ID(HttpStatus.BAD_REQUEST, "유효하지 않은 메뉴 ID입니다."),
    NOT_FOUND_ORDER(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다."),
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "잘못된 입력입니다."),
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "인가되지 않은 접근입니다."),

    // REVIEW
    DUPLICATE_REVIEW_ID(HttpStatus.CONFLICT, "이미 리뷰가 존재합니다."),
    NOT_FOUND_REVIEW(HttpStatus.NOT_FOUND, "존재하지 않는 리뷰입니다."),
    NO_AUTHENTICATION(HttpStatus.UNAUTHORIZED, "권한이 없습니다."),
    INVALID_ORDER_STATUS(HttpStatus.NOT_ACCEPTABLE, "리뷰를 생성 가능한 주문 상태가 아닙니다."),

    // JWT
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 리프레시 토큰입니다. 다시 로그인 해주세요."),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다. 다시 로그인 해주세요."),

    REQUIRES_LOGIN(HttpStatus.LOCKED, "로그인이 필요한 서비스입니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}