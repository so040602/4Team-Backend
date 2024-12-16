package com.recipe.Repository;

import com.recipe.entity.Member;
import com.recipe.entity.Review;
import com.recipe.entity.ReviewView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface ReviewViewRepository extends JpaRepository<ReviewView, Long> {

    boolean existsByReview_IdAndMember_MemberIdAndViewedAtAfter(Long reviewId, Long memberId, LocalDateTime date);

    void deleteByReview_IdAndMember_MemberId(Long reviewId, Long memberId);

    ReviewView findByReviewAndMember(Review review, Member member);
}
