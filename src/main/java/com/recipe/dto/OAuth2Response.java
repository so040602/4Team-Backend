package com.recipe.dto;

import com.recipe.entity.SocialProvider;

public interface OAuth2Response {
    // 소셜 로그인 제공자
    SocialProvider getProvider();

    // 소셜 서비스의 사용자 고유 ID
    String getProviderId();

    // 소셜 계정 이메일
    String getEmail();

    // 소셜 계정 이름
    String getName();
}
