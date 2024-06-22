package com.sparta.javajyojo.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class ProfileRequestDto {
    private String password;

    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,15}$",
            message = "최소 8자 이상, 15자 이하이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수문자")
    private String newPassword;

    private String name;
    private String intro;
}
