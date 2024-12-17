package com.recipe.controller;

import com.recipe.dto.LoginRequest;
import com.recipe.dto.LoginResponse;
import com.recipe.dto.MemberDTO;
import com.recipe.dto.SignupRequest;
import com.recipe.dto.ApiResponse;
import com.recipe.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<MemberDTO> signup(@RequestBody SignupRequest request) {
        return ResponseEntity.ok(memberService.signup(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(memberService.login(request));
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<ApiResponse<MemberDTO>> getMemberInfo(@PathVariable Long memberId) {
        MemberDTO memberDTO = memberService.getMemberInfo(memberId);
        return ResponseEntity.ok(ApiResponse.success(memberDTO));
    }

    @GetMapping("/profile/{memberId}")
    public ResponseEntity<ApiResponse<MemberDTO>> getMemberProfile(@PathVariable Long memberId) {
        MemberDTO memberDTO = memberService.getMemberProfile(memberId);
        return ResponseEntity.ok(ApiResponse.success(memberDTO));
    }

    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmailDuplicate(@RequestParam String email) {
        return ResponseEntity.ok(memberService.checkEmailDuplicate(email));
    }

    @GetMapping("/check-displayname")
    public ResponseEntity<Boolean> checkDisplayNameDuplicate(@RequestParam String displayName) {
        return ResponseEntity.ok(memberService.checkDisplayNameDuplicate(displayName));
    }
}
