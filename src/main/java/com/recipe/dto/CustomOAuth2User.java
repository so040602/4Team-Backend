package com.recipe.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {
    private final MemberDTO memberDTO;

    public CustomOAuth2User(MemberDTO memberDTO) {
        this.memberDTO = memberDTO;
    }

    @Override
    public Map<String, Object> getAttributes() {
        // OAuth2 로그인 시에만 사용되므로, 실제로는 사용되지 않음
        return Collections.emptyMap();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(memberDTO.getRole()));
    }

    @Override
    public String getName() {
        return memberDTO.getDisplayName();
    }

    // 추가 getter 메소드들
    public Long getMemberId() {
        return memberDTO.getMemberId();
    }

    public String getPrimaryEmail() {
        return memberDTO.getPrimaryEmail();
    }

    public String getDisplayName() {
        return memberDTO.getDisplayName();
    }

    public String getRole() {
        return memberDTO.getRole();
    }

    public MemberDTO getMemberDTO() {
        return memberDTO;
    }
}
