package com.sparta.javajyojo.auth;


import com.sparta.javajyojo.entity.User;
import com.sparta.javajyojo.enums.ErrorType;
import com.sparta.javajyojo.exception.CustomException;
import com.sparta.javajyojo.jwt.JwtUtil;
import com.sparta.javajyojo.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    @Transactional
    public void updateRefreshToken(Long id, String refreshToken) {
        User user = userRepository.findById(id).orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_USER));
        user.updateToken(refreshToken);
        userRepository.save(user);
    }


    public String reissueAccessToken(Long id, HttpServletRequest request, HttpServletResponse response) {

        // 1. header 에 담긴 access token 유효기간 확인.
        String headerAccessToken = jwtUtil.getAccessTokenFromRequest(request);
        headerAccessToken = jwtUtil.substringToken(headerAccessToken);

        Claims accessClaims = jwtUtil.getUserInfoFromToken(headerAccessToken);
        Date accessExp = accessClaims.getExpiration();
        Date date = new Date();
        if(!accessExp.before(date)) {
            throw new CustomException(ErrorType.VALID_ACCESS_TOKEN); // 토큰이 아직 유효 합니다.
        }

        // 2. refresh token 존재여부 확인
        String headerRefreshToken = jwtUtil.getRefreshTokenFromRequest(request);

        if (headerRefreshToken == null) {
            throw new CustomException(ErrorType.INVALID_REFRESH_TOKEN); // 리프레시 토큰을 찾을 수 없습니다.
        }

        // 3. DB에 저장된 refresh token 이 동일한지 확인
        User user = userRepository.findById(id).orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_USER));
        if(!headerRefreshToken.equals(user.getRefreshToken())) {
            throw new CustomException(ErrorType.INVALID_REFRESH_TOKEN); // 동일한 리프레시 토큰이 아닙니다.
        }

        // 4. refresh token 유효기간 확인
        headerRefreshToken = jwtUtil.substringToken(headerRefreshToken);
        Claims refreshClaims = jwtUtil.getUserInfoFromToken(headerRefreshToken);
        Date refreshExp = refreshClaims.getExpiration();

        if (refreshExp.after(date)) {
            throw new CustomException(ErrorType.INVALID_REFRESH_TOKEN); // 리프레시 토큰 유효기간이 지났습니다.
        }

        // 5. access token 재발급
        String newAccessToken = jwtUtil.createAccessToken(user.getUsername(), user.getRole());

        return newAccessToken;
    }
}
