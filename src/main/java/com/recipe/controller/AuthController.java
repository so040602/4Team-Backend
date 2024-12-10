package com.recipe.controller;

import com.recipe.dto.*;
import com.recipe.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<MemberDTO>> signup(@Valid @RequestBody SignupRequest request) {
        MemberDTO memberDTO = memberService.signup(request);
        return ResponseEntity.ok(ApiResponse.success("회원가입이 완료되었습니다", memberDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = memberService.login(request);
        return ResponseEntity.ok(ApiResponse.success("로그인이 완료되었습니다", response));
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<ApiResponse<LoginResponse>> refreshToken(
            @CookieValue("Authorization") String token,
            HttpServletResponse response) {
        LoginResponse loginResponse = memberService.refreshToken(token);

        // 기존 쿠키 삭제
        Cookie cookie = new Cookie("Authorization", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok(ApiResponse.success("토큰이 갱신되었습니다", loginResponse));
    }

    @GetMapping("/check/email")
    public ResponseEntity<ApiResponse<Boolean>> checkEmailDuplicate(@RequestParam String email) {
        boolean isDuplicate = memberService.checkEmailDuplicate(email);
        return ResponseEntity.ok(ApiResponse.success(
                isDuplicate ? "이미 사용중인 이메일입니다" : "사용 가능한 이메일입니다",
                isDuplicate
        ));
    }

    @GetMapping("/check/displayName")
    public ResponseEntity<ApiResponse<Boolean>> checkDisplayNameDuplicate(@RequestParam String displayName) {
        boolean isDuplicate = memberService.checkDisplayNameDuplicate(displayName);
        return ResponseEntity.ok(ApiResponse.success(
                isDuplicate ? "이미 사용중인 닉네임입니다" : "사용 가능한 닉네임입니다",
                isDuplicate
        ));
    }
}
