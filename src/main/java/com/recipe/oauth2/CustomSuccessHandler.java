package com.recipe.oauth2;

import com.recipe.dto.CustomOAuth2User;
import com.recipe.jwt.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        String token = jwtUtil.createJwt(
                customUserDetails.getMemberId(),
                customUserDetails.getPrimaryEmail(),
                customUserDetails.getDisplayName(),
                customUserDetails.getRole(),
                1000 * 60 * 5L  // 5분으로 줄임 (프론트엔드에서 갱신할 시간 필요)
        );

        // 임시 토큰을 쿠키에 저장
        Cookie cookie = new Cookie("Authorization", token);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(300); // 5분
        response.addCookie(cookie);

        // 프론트엔드로 리다이렉트
        getRedirectStrategy().sendRedirect(request, response, "http://localhost:3000/oauth2/callback");
    }
}
