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

    @DeleteMapping("/sign-out")
    public ResponseEntity<String> signOut(
            @RequestParam Long userId) {

        userService.signOut(userId);
        return ResponseEntity.ok().body("회원 탈퇴에 성공했습니다.");
    }

//    @DeleteMapping("/sign-out")
//    public ResponseEntity<String> signOut(
//            @AuthenticationPrincipal UserDetailsImpl userDetails) {
//
//        userService.signOut(userDetails.getUser().getId());
//        return ResponseEntity.ok().body("회원 탈퇴에 성공했습니다.");
//    }

    @DeleteMapping("/log-out")
    public ResponseEntity<String> logOut(
            @RequestParam Long userId) {

        userService.logOut(userId);
        return ResponseEntity.ok().body("로그아웃 성공하셨습니다");
    }

//    @DeleteMapping("/log-out")
//    public ResponseEntity<String> logOut(
//            @AuthenticationPrincipal UserDetailsImpl userDetails) {
//
//        userService.logOut(userDetails.getUser().getId());
//        return ResponseEntity.ok().body("로그아웃 성공하셨습니다");
//    }

    @GetMapping
    public ResponseEntity<ProfileResponseDto> getProfile(
            @RequestParam Long userId) {

        return ResponseEntity.ok().body(userService.getProfile(userId));
    }

//    @GetMapping
//    public ResponseEntity<ProfileResponseDto> getProfile(
//            @AuthenticationPrincipal UserDetailsImpl userDetails) {
//
//        return ResponseEntity.ok().body(userService.getProfile(userDetails.getUser().getId()));
//    }

    @PatchMapping
    public ResponseEntity<ProfileResponseDto> profileUpdate(
            @RequestParam Long userId,
            @Valid @RequestBody ProfileRequestDto requestDto) {

        return ResponseEntity.ok().body(userService.update(userId, requestDto));
    }

//    @PatchMapping
//    public ResponseEntity<ProfileResponseDto> profileUpdate(
//            @AuthenticationPrincipal UserDetailsImpl userDetails,
//            @Valid @RequestBody ProfileRequestDto requestDto) {
//
//        return ResponseEntity.ok().body(userService.update(userDetails.getUser().getId(), requestDto));
//    }

}
