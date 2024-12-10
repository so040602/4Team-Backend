package com.recipe.dto;

import com.recipe.entity.SocialProvider;

import java.util.Map;

public class KakaoResponse implements OAuth2Response {

    private final Map<String, Object> attribute;
    private final Map<String, Object> kakaoAccountAttribute;

    public KakaoResponse(Map<String, Object> attribute) {
        this.attribute = attribute;
        this.kakaoAccountAttribute = (Map<String, Object>) attribute.get("kakao_account");
    }

    @Override
    public SocialProvider getProvider() {
        return SocialProvider.KAKAO;
    }

    @Override
    public String getProviderId() {
        return attribute.get("id").toString();
    }

    @Override
    public String getEmail() {
        return kakaoAccountAttribute.get("email").toString();
    }

    @Override
    public String getName() {
        if (kakaoAccountAttribute != null && kakaoAccountAttribute.get("profile") != null) {
            Map<String, Object> profile = (Map<String, Object>) kakaoAccountAttribute.get("profile");
            if (profile != null && profile.get("nickname") != null) {
                return profile.get("nickname").toString();
            }
        }
        return "Unknown";  // nickname이 없거나 profile이 null인 경우
    }
}
