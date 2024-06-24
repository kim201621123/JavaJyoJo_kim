package com.sparta.javajyojo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.javajyojo.jwt.JwtUtil;
import com.sparta.javajyojo.service.KakaoService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/kakao")
@RequiredArgsConstructor
public class KakaoController {

    private final KakaoService kakaoService;

    @GetMapping("/callback")
    public ResponseEntity<String> kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        List<String> token = kakaoService.kakaoLogin(code);

        String access = token.get(0);
        String refresh = token.get(1);

        Cookie accessCookie = new Cookie(JwtUtil.ACCESS_TOKEN_HEADER, access.substring(7));
        Cookie refreshCookie = new Cookie(JwtUtil.REFRESH_TOKEN_HEADER, refresh.substring(7));

        accessCookie.setPath("/");
        refreshCookie.setPath("/");

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);

        return ResponseEntity.ok().body("로그인 성공");
    }
}