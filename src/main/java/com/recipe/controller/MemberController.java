package com.recipe.controller;

import com.recipe.dto.LoginRequest;
import com.recipe.dto.LoginResponse;
import com.recipe.dto.MemberDTO;
import com.recipe.dto.SignupRequest;
import com.recipe.dto.ApiResponse;
import com.recipe.entity.Member;
import com.recipe.entity.MemberGrade;
import com.recipe.service.MemberService;
import com.recipe.service.MemberGradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class MemberController {

    private final MemberService memberService;
    private final MemberGradeService memberGradeService;

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

    @GetMapping("/{memberId}/grade")
    public ResponseEntity<MemberGrade> getMemberGrade(@PathVariable Long memberId) {
        MemberDTO memberDTO = memberService.getMemberProfile(memberId);
        return ResponseEntity.ok(memberDTO.getGrade());
    }

    @GetMapping("/grade/all")
    public ResponseEntity<List<MemberGrade>> getAllGrades() {
        return ResponseEntity.ok(memberGradeService.getAllGrades());
    }

    @PostMapping("/grade/update-all")
    public ResponseEntity<String> updateAllMembersGrade() {
        memberService.updateAllMembersGrade();
        return ResponseEntity.ok("모든 회원의 등급이 업데이트되었습니다.");
    }

    @PutMapping("/{memberId}/displayName")
    public ResponseEntity<ApiResponse<MemberDTO>> updateDisplayName(
            @PathVariable Long memberId,
            @RequestBody Map<String, String> request) {
        String newDisplayName = request.get("displayName");
        MemberDTO updatedMember = memberService.updateDisplayName(memberId, newDisplayName);
        return ResponseEntity.ok(ApiResponse.success(updatedMember));
    }
}
