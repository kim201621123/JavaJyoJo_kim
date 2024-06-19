package com.sparta.javajyojo.controller;

import com.sparta.javajyojo.dto.ProfileRequestDto;
import com.sparta.javajyojo.dto.ProfileResponseDto;
import com.sparta.javajyojo.dto.SignUpRequestDto;
import com.sparta.javajyojo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping("sign-up")
    public ResponseEntity<ProfileResponseDto> signUp(
            @Valid @RequestBody SignUpRequestDto requestDto) {

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.signUp(requestDto));
    }

//    @DeleteMapping("/log-out")
    @DeleteMapping("/log-out/{userId}")
    public ResponseEntity<String> logOut(
//            @AuthenticationPrincipal UserDetailsImpl userDetails) {
            @PathVariable Long userId) {

//        userService.logOut(userDetails.getUser().getId());
        userService.logOut(userId);
        return ResponseEntity.ok().body("로그아웃 성공하셨습니다");
    }

//    @PatchMapping
    @PatchMapping("/{userId}")
    public ResponseEntity<ProfileResponseDto> profileUpdate(
//            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long userId,
            @RequestBody ProfileRequestDto requestDto) {

//        return ResponseEntity.ok().body(userService.update(userDetails.getUser().getId(), requestDto));
        return ResponseEntity.ok().body(userService.update(userId, requestDto));
    }

}
