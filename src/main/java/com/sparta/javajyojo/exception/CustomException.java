package com.sparta.javajyojo.exception;

import com.sparta.javajyojo.enums.ErrorType;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final String result;
    private final ErrorType errorType;

    public CustomException(ErrorType errorType) {
        this.result = "ERROR";
        this.errorType = errorType;
    }
}
