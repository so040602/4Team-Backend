package com.recipe.service;

import com.recipe.Repository.MemberGradeRepository;
import com.recipe.Repository.MemberRepository;
import com.recipe.entity.Member;
import com.recipe.entity.MemberGrade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberGradeService {
    private final MemberRepository memberRepository;
    private final MemberGradeRepository memberGradeRepository;

    @Transactional
    public void updateMemberGrade(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        // 회원의 활동 수 기준으로 적절한 등급 찾기
        MemberGrade appropriateGrade = memberGradeRepository.findFirstByRequiredRecipeCountLessThanEqualAndRequiredReviewCountLessThanEqualAndRequiredCommentCountLessThanEqualOrderByGradeLevelDesc(
                member.getRecipeCount(),
                member.getReviewCount(),
                member.getCommentCount()
        ).orElse(getDefaultGrade());

        // 등급 업데이트
        member.setGrade(appropriateGrade);
        memberRepository.save(member);
    }

    private MemberGrade getDefaultGrade() {
        return memberGradeRepository.findByGradeLevel(1)
                .orElseThrow(() -> new RuntimeException("Default grade not found"));
    }

    @Transactional
    public void incrementRecipeCount(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        member.setRecipeCount(member.getRecipeCount() + 1);
        memberRepository.save(member);
        updateMemberGrade(memberId);
    }

    @Transactional
    public void incrementReviewCount(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        member.setReviewCount(member.getReviewCount() + 1);
        memberRepository.save(member);
        updateMemberGrade(memberId);
    }

    @Transactional
    public void incrementCommentCount(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        member.setCommentCount(member.getCommentCount() + 1);
        memberRepository.save(member);
        updateMemberGrade(memberId);
    }

    public List<MemberGrade> getAllGrades() {
        return memberGradeRepository.findAll();
    }
}
