package com.recipe.Repository;

import com.recipe.entity.SocialConnection;
import com.recipe.entity.SocialProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SocialConnectionRepository extends JpaRepository<SocialConnection, Long> {
    // 특정 소셜 로그인 제공자와 ID로 연동 정보 찾기
    Optional<SocialConnection> findByProviderAndProviderId(SocialProvider provider, String providerId);

    // 특정 회원의 모든 소셜 연동 정보 찾기
    List<SocialConnection> findByMemberMemberId(Long memberId);

    // 특정 회원의 특정 제공자 연동 정보 찾기
    Optional<SocialConnection> findByMemberMemberIdAndProvider(Long memberId, SocialProvider provider);

    // 특정 소셜 연동 정보가 이미 존재하는지 확인
    boolean existsByProviderAndProviderId(SocialProvider provider, String providerId);

    // 특정 회원의 특정 제공자 연동 정보가 존재하는지 확인
    boolean existsByMemberMemberIdAndProvider(Long memberId, SocialProvider provider);
}
