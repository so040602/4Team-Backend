package com.recipe.service;

import com.recipe.Repository.MemberGradeRepository;
import com.recipe.dto.LoginRequest;
import com.recipe.dto.LoginResponse;
import com.recipe.dto.MemberDTO;
import com.recipe.dto.SignupRequest;
import com.recipe.entity.Member;
import com.recipe.entity.MemberGrade;
import com.recipe.jwt.JWTUtil;
import com.recipe.Repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberGradeRepository memberGradeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;

    @Transactional
    public MemberDTO signup(SignupRequest request) {
        // 이메일 중복 체크
        if (memberRepository.findByPrimaryEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 사용중인 이메일입니다");
        }

        // 닉네임 중복 체크
        if (memberRepository.findByDisplayName(request.getDisplayName()).isPresent()) {
            throw new IllegalArgumentException("이미 사용중인 닉네임입니다");
        }

        // 기본 등급 가져오기 (새싹)
        MemberGrade defaultGrade = memberGradeRepository.findByGradeLevel(1)
                .orElseThrow(() -> new RuntimeException("기본 등급을 찾을 수 없습니다"));

        // 새 회원 생성
        Member member = Member.builder()
                .primaryEmail(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .displayName(request.getDisplayName())
                .role("ROLE_USER")
                .grade(defaultGrade)
                .build();

        Member savedMember = memberRepository.save(member);

        return MemberDTO.builder()
                .memberId(savedMember.getMemberId())
                .primaryEmail(savedMember.getPrimaryEmail())
                .displayName(savedMember.getDisplayName())
                .role(savedMember.getRole())
                .grade(savedMember.getGrade())
                .build();
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        // 이메일로 회원 찾기
        Member member = memberRepository.findByPrimaryEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("이메일 또는 비밀번호가 일치하지 않습니다"));

        // 소셜 로그인 회원인지 확인
        if (!member.getSocialConnections().isEmpty()) {
            // 첫 번째 소셜 연동 정보의 제공자 확인
            String provider = member.getSocialConnections().stream()
                    .findFirst()
                    .map(conn -> conn.getProvider().toString().toLowerCase())
                    .map(providerName -> {
                        switch (providerName) {
                            case "naver": return "네이버";
                            case "kakao": return "카카오";
                            case "google": return "구글";
                            default: return providerName;
                        }
                    })
                    .orElse("소셜");

            throw new BadCredentialsException(
                    String.format("이 계정은 %s 로그인으로 가입된 계정입니다. %s 로그인을 이용해주세요.",
                            provider, provider)
            );
        }

        // 비밀번호 확인
        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new BadCredentialsException("이메일 또는 비밀번호가 일치하지 않습니다");
        }

        // JWT 토큰 생성
        String token = jwtUtil.createJwt(
                member.getMemberId(),
                member.getPrimaryEmail(),
                member.getDisplayName(),
                member.getRole(),
                1000 * 60 * 60 * 24L // 24시간
        );

        // 응답 데이터 생성
        MemberDTO memberDTO = MemberDTO.builder()
                .memberId(member.getMemberId())
                .primaryEmail(member.getPrimaryEmail())
                .displayName(member.getDisplayName())
                .role(member.getRole())
                .build();

        return LoginResponse.builder()
                .token(token)
                .member(memberDTO)
                .build();
    }

    @Transactional(readOnly = true)
    public LoginResponse refreshToken(String token) {
        // 토큰 유효성 검사
        if (jwtUtil.isExpired(token)) {
            throw new BadCredentialsException("만료된 토큰입니다");
        }

        // 토큰에서 사용자 정보 추출
        Long memberId = jwtUtil.getMemberId(token);

        // 회원 정보 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BadCredentialsException("존재하지 않는 회원입니다"));

        // 새로운 JWT 토큰 생성
        String newToken = jwtUtil.createJwt(
                member.getMemberId(),
                member.getPrimaryEmail(),
                member.getDisplayName(),
                member.getRole(),
                1000 * 60 * 60 * 24L // 24시간
        );

        // 응답 데이터 생성
        MemberDTO memberDTO = MemberDTO.builder()
                .memberId(member.getMemberId())
                .primaryEmail(member.getPrimaryEmail())
                .displayName(member.getDisplayName())
                .role(member.getRole())
                .build();

        return LoginResponse.builder()
                .token(newToken)
                .member(memberDTO)
                .build();
    }

    @Transactional(readOnly = true)
    public boolean checkEmailDuplicate(String email) {
        return memberRepository.findByPrimaryEmail(email).isPresent();
    }

    @Transactional(readOnly = true)
    public boolean checkDisplayNameDuplicate(String displayName) {
        return memberRepository.findByDisplayName(displayName).isPresent();
    }

    @Transactional(readOnly = true)
    public Member findById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found with id: " + memberId));
    }

    @Transactional(readOnly = true)
    public MemberDTO getMemberInfo(Long memberId) {
        Member member = findById(memberId);

        return MemberDTO.builder()
                .memberId(member.getMemberId())
                .primaryEmail(member.getPrimaryEmail())
                .displayName(member.getDisplayName())
                .role(member.getRole())
                .createdAt(member.getCreatedAt().toLocalDateTime())
                .updatedAt(member.getUpdatedAt().toLocalDateTime())
                .build();
    }

    @Transactional(readOnly = true)
    public MemberDTO getMemberProfile(Long memberId) {
        Member member = findById(memberId);

        return MemberDTO.builder()
                .memberId(member.getMemberId())
                .primaryEmail(member.getPrimaryEmail())
                .displayName(member.getDisplayName())
                .role(member.getRole())
                .grade(member.getGrade())
                .createdAt(member.getCreatedAt().toLocalDateTime())
                .updatedAt(member.getUpdatedAt().toLocalDateTime())
                .build();
    }

    @Transactional
    public void updateAllMembersGrade() {
        List<Member> members = memberRepository.findAll();
        for (Member member : members) {
            // 회원의 활동 수 기준으로 적절한 등급 찾기
            MemberGrade appropriateGrade = memberGradeRepository.findFirstByRequiredRecipeCountLessThanEqualAndRequiredReviewCountLessThanEqualAndRequiredCommentCountLessThanEqualOrderByGradeLevelDesc(
                    member.getRecipeCount(),
                    member.getReviewCount(),
                    member.getCommentCount()
            ).orElseGet(() -> memberGradeRepository.findByGradeLevel(1)
                    .orElseThrow(() -> new RuntimeException("기본 등급을 찾을 수 없습니다")));

            // 등급 업데이트
            member.setGrade(appropriateGrade);
            memberRepository.save(member);
        }
    }
}
