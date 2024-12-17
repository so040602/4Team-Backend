package com.recipe.controller;

import com.recipe.dto.ApiResponse;
import com.recipe.dto.CustomOAuth2User;
import com.recipe.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/follows")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @PostMapping("/{followingId}")
    public ResponseEntity<ApiResponse<Void>> follow(
            @AuthenticationPrincipal CustomOAuth2User user,
            @PathVariable Long followingId) {
        if (user == null) {
            throw new IllegalStateException("로그인이 필요합니다");
        }
        followService.follow(user.getMemberId(), followingId);
        return ResponseEntity.ok(ApiResponse.success("팔로우했습니다", null));
    }

    @DeleteMapping("/{followingId}")
    public ResponseEntity<ApiResponse<Void>> unfollow(
            @AuthenticationPrincipal CustomOAuth2User user,
            @PathVariable Long followingId) {
        if (user == null) {
            throw new IllegalStateException("로그인이 필요합니다");
        }
        followService.unfollow(user.getMemberId(), followingId);
        return ResponseEntity.ok(ApiResponse.success("팔로우를 취소했습니다", null));
    }

    @GetMapping("/check/{followingId}")
    public ResponseEntity<ApiResponse<Boolean>> checkFollow(
            @AuthenticationPrincipal CustomOAuth2User user,
            @PathVariable Long followingId) {
        boolean isFollowing = false;
        if (user != null) {
            isFollowing = followService.isFollowing(user.getMemberId(), followingId);
        }
        return ResponseEntity.ok(ApiResponse.success("팔로우 여부를 확인했습니다", isFollowing));
    }
}
