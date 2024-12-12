package com.recipe.Repository;

import com.recipe.entity.ReviewView;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewViewRepository extends JpaRepository<ReviewView, Long> {
    boolean existsByReview_IdAndMember_MemberId(Long reviewId, Long memberId);
}
