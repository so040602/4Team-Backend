package com.recipe.jwt;

import com.recipe.dto.CustomOAuth2User;
import com.recipe.dto.MemberDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Authorization 쿠키 찾기
        String authorization = null;
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("Authorization")) {
                    authorization = cookie.getValue();
                    break;
                }
            }
        }

        // 토큰이 없거나 만료된 경우
        if (authorization == null || jwtUtil.isExpired(authorization)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰에서 사용자 정보 추출
        Long memberId = jwtUtil.getMemberId(authorization);
        String primaryEmail = jwtUtil.getPrimaryEmail(authorization);
        String displayName = jwtUtil.getDisplayName(authorization);
        String role = jwtUtil.getRole(authorization);

        // MemberDTO 생성
        MemberDTO memberDTO = MemberDTO.builder()
                .memberId(memberId)
                .primaryEmail(primaryEmail)
                .displayName(displayName)
                .role(role)
                .build();

        // CustomOAuth2User 생성 및 인증 처리
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(memberDTO);
        Authentication authToken = new UsernamePasswordAuthenticationToken(
                customOAuth2User,
                null,
                customOAuth2User.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request, response);
    }
}
