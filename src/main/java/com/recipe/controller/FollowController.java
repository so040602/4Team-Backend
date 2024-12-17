package com.recipe.controller;

import com.recipe.dto.ApiResponse;
import com.recipe.dto.CustomOAuth2User;
import com.recipe.dto.MemberDTO;
import com.recipe.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/follow")
@RequiredArgsConstructor
public class FollowController {
    private final FollowService followService;

    @PostMapping("/{followingId}")
    public ResponseEntity<ApiResponse<Void>> follow(
            @PathVariable Long followingId,
            @AuthenticationPrincipal CustomOAuth2User user) {
        if (user == null) {
            throw new IllegalStateException("로그인이 필요합니다");
        }
        followService.follow(user.getMemberId(), followingId);
        return ResponseEntity.ok(ApiResponse.success("팔로우했습니다", null));
    }

    @DeleteMapping("/{followingId}")
    public ResponseEntity<ApiResponse<Void>> unfollow(
            @PathVariable Long followingId,
            @AuthenticationPrincipal CustomOAuth2User user) {
        if (user == null) {
            throw new IllegalStateException("로그인이 필요합니다");
        }
        followService.unfollow(user.getMemberId(), followingId);
        return ResponseEntity.ok(ApiResponse.success("팔로우를 취소했습니다", null));
    }

    @GetMapping("/check/{followingId}")
    public ResponseEntity<ApiResponse<Boolean>> checkFollow(
            @PathVariable Long followingId,
            @AuthenticationPrincipal CustomOAuth2User user) {
        boolean isFollowing = false;
        if (user != null) {
            isFollowing = followService.isFollowing(user.getMemberId(), followingId);
        }
        return ResponseEntity.ok(ApiResponse.success("팔로우 여부를 확인했습니다", isFollowing));
    }

    @GetMapping("/count/{memberId}")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getFollowCounts(@PathVariable Long memberId) {
        long followerCount = followService.getFollowerCount(memberId);
        long followingCount = followService.getFollowingCount(memberId);

        Map<String, Long> counts = Map.of(
                "followerCount", followerCount,
                "followingCount", followingCount
        );

        return ResponseEntity.ok(ApiResponse.success("팔로우 카운트를 조회했습니다", counts));
    }

    @GetMapping("/followers/{memberId}")
    public ResponseEntity<ApiResponse<List<MemberDTO>>> getFollowers(@PathVariable Long memberId) {
        List<MemberDTO> followers = followService.getFollowers(memberId);
        return ResponseEntity.ok(ApiResponse.success("팔로워 목록을 조회했습니다", followers));
    }

    @GetMapping("/following/{memberId}")
    public ResponseEntity<ApiResponse<List<MemberDTO>>> getFollowing(@PathVariable Long memberId) {
        List<MemberDTO> following = followService.getFollowing(memberId);
        return ResponseEntity.ok(ApiResponse.success("팔로잉 목록을 조회했습니다", following));
    }
}
