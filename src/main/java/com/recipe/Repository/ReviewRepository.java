package com.recipe.Repository;

import com.recipe.entity.Member;
import com.recipe.entity.Review;
import com.recipe.entity.ReviewComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    // Custom query methods can be added here

    @Query("SELECT rc FROM ReviewComment rc WHERE rc.review.id = :reviewId")
    List<ReviewComment> findCommentsByReviewId(@Param("reviewId") Long reviewId);

    // 회원이 작성한 리뷰 목록 조회 (작성일 기준 내림차순)
    List<Review> findByMemberOrderByCreatedAtDesc(Member member);
}