package com.recipe.service;

import com.recipe.Repository.FollowRepository;
import com.recipe.Repository.MemberRepository;
import com.recipe.dto.MemberDTO;
import com.recipe.entity.Follow;
import com.recipe.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void follow(Long followerId, Long followingId) {
        if (followerId.equals(followingId)) {
            throw new IllegalArgumentException("자기 자신을 팔로우할 수 없습니다");
        }

        Member follower = memberRepository.findById(followerId)
                .orElseThrow(() -> new IllegalArgumentException("팔로워를 찾을 수 없습니다"));
        Member following = memberRepository.findById(followingId)
                .orElseThrow(() -> new IllegalArgumentException("팔로잉할 사용자를 찾을 수 없습니다"));

        if (followRepository.existsByFollowerAndFollowing(follower, following)) {
            throw new IllegalArgumentException("이미 팔로우한 사용자입니다");
        }

        Follow follow = Follow.builder()
                .follower(follower)
                .following(following)
                .build();

        followRepository.save(follow);
    }

    @Transactional
    public void unfollow(Long followerId, Long followingId) {
        if (followerId.equals(followingId)) {
            throw new IllegalArgumentException("자기 자신을 언팔로우할 수 없습니다");
        }

        Member follower = memberRepository.findById(followerId)
                .orElseThrow(() -> new IllegalArgumentException("팔로워를 찾을 수 없습니다"));
        Member following = memberRepository.findById(followingId)
                .orElseThrow(() -> new IllegalArgumentException("언팔로우할 사용자를 찾을 수 없습니다"));

        // 먼저 팔로우 관계가 있는지 확인
        boolean exists = followRepository.existsByFollowerAndFollowing(follower, following);
        if (!exists) {
            throw new IllegalArgumentException(
                    String.format("사용자 %s님은 %s님을 팔로우하고 있지 않습니다",
                            follower.getDisplayName(), following.getDisplayName())
            );
        }

        Follow follow = followRepository.findByFollowerAndFollowing(follower, following)
                .orElseThrow(() -> new IllegalStateException("팔로우 관계 조회 중 오류가 발생했습니다"));

        followRepository.delete(follow);
    }

    @Transactional(readOnly = true)
    public boolean isFollowing(Long followerId, Long followingId) {
        Member follower = memberRepository.findById(followerId)
                .orElseThrow(() -> new IllegalArgumentException("팔로워를 찾을 수 없습니다"));
        Member following = memberRepository.findById(followingId)
                .orElseThrow(() -> new IllegalArgumentException("팔로잉할 사용자를 찾을 수 없습니다"));

        return followRepository.existsByFollowerAndFollowing(follower, following);
    }

    @Transactional(readOnly = true)
    public long getFollowerCount(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));
        return followRepository.countByFollowing(member);
    }

    @Transactional(readOnly = true)
    public long getFollowingCount(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));
        return followRepository.countByFollower(member);
    }

    @Transactional(readOnly = true)
    public List<MemberDTO> getFollowers(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));

        List<Follow> followers = followRepository.findByFollowing(member);
        return followers.stream()
                .map(follow -> {
                    Member follower = follow.getFollower();
                    boolean isFollowing = isFollowing(memberId, follower.getMemberId());
                    return MemberDTO.builder()
                            .memberId(follower.getMemberId())
                            .displayName(follower.getDisplayName())
                            .isFollowing(isFollowing)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MemberDTO> getFollowing(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));

        List<Follow> following = followRepository.findByFollower(member);
        return following.stream()
                .map(follow -> {
                    Member followingMember = follow.getFollowing();
                    return MemberDTO.builder()
                            .memberId(followingMember.getMemberId())
                            .displayName(followingMember.getDisplayName())
                            .isFollowing(true)  // 팔로잉 목록이므로 항상 true
                            .build();
                })
                .collect(Collectors.toList());
    }
}
