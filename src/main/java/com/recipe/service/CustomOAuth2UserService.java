package com.recipe.service;

import com.recipe.Repository.MemberRepository;
import com.recipe.Repository.SocialConnectionRepository;
import com.recipe.dto.*;
import com.recipe.entity.Member;
import com.recipe.entity.SocialConnection;
import com.recipe.entity.SocialProvider;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;
    private final SocialConnectionRepository socialConnectionRepository;

    public CustomOAuth2UserService(MemberRepository memberRepository,
                                   SocialConnectionRepository socialConnectionRepository) {
        this.memberRepository = memberRepository;
        this.socialConnectionRepository = socialConnectionRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 소셜 로그인 제공자 확인
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        SocialProvider authProvider = null;

        // 제공자별 응답 처리 및 SocialProvider 설정
        switch (registrationId.toLowerCase()) {
            case "naver":
                oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
                authProvider = SocialProvider.NAVER;
                break;
            case "google":
                oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
                authProvider = SocialProvider.GOOGLE;
                break;
            case "kakao":
                oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
                authProvider = SocialProvider.KAKAO;
                break;
            default:
                throw new OAuth2AuthenticationException("Unsupported provider: " + registrationId);
        }

        // 소셜 연동 정보 확인
        Optional<SocialConnection> socialConnection =
                socialConnectionRepository.findByProviderAndProviderId(
                        authProvider,
                        oAuth2Response.getProviderId()
                );

        Member member;
        if (socialConnection.isPresent()) {
            // 기존 연동 정보가 있는 경우
            member = socialConnection.get().getMember();
            updateSocialConnection(socialConnection.get(), oAuth2Response);
        } else {
            // 새로운 사용자 또는 새로운 소셜 연동
            member = findOrCreateMember(oAuth2Response);
            createSocialConnection(member, authProvider, oAuth2Response);
        }

        // MemberDTO 생성 및 반환
        MemberDTO memberDTO = MemberDTO.builder()
                .memberId(member.getMemberId())
                .primaryEmail(member.getPrimaryEmail())
                .displayName(member.getDisplayName())
                .role(member.getRole())
                .build();

        return new CustomOAuth2User(memberDTO);
    }

    private Member findOrCreateMember(OAuth2Response oAuth2Response) {
        // 이메일로 기존 회원 찾기
        return memberRepository.findByPrimaryEmail(oAuth2Response.getEmail())
                .orElseGet(() -> createNewMember(oAuth2Response));
    }

    private Member createNewMember(OAuth2Response oAuth2Response) {
        Member member = Member.builder()
                .displayName(oAuth2Response.getName())
                .primaryEmail(oAuth2Response.getEmail())
                .role("ROLE_USER")
                .build();
        return memberRepository.save(member);
    }

    private void createSocialConnection(Member member, SocialProvider provider, OAuth2Response oAuth2Response) {
        SocialConnection socialConnection = SocialConnection.builder()
                .member(member)
                .provider(provider)
                .providerId(oAuth2Response.getProviderId())
                .socialEmail(oAuth2Response.getEmail())
                .socialName(oAuth2Response.getName())
                .build();
        socialConnectionRepository.save(socialConnection);
    }

    private void updateSocialConnection(SocialConnection socialConnection, OAuth2Response oAuth2Response) {
        socialConnection.setSocialEmail(oAuth2Response.getEmail());
        socialConnection.setSocialName(oAuth2Response.getName());
        socialConnectionRepository.save(socialConnection);
    }
}
