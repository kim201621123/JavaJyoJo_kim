package com.sparta.javajyojo.dto;

import lombok.Getter;

@Getter
public class KakaoTokenDto {

    private String accessToken;
    private String refreshToken;

    public KakaoTokenDto(String accessToken, String refreshToken){
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

}
