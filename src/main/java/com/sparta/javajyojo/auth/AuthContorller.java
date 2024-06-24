package com.sparta.javajyojo.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthContorller {

    private final AuthService authService;

    @GetMapping("/auth/{id}")
    public ResponseEntity<String> reissueToken(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
        String newAccessToken = authService.reissueAccessToken(id, request, response);
        return new ResponseEntity<>(newAccessToken, HttpStatus.OK);
    }
}
