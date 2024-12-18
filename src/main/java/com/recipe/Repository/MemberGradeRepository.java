package com.recipe.Repository;

import com.recipe.entity.MemberGrade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberGradeRepository extends JpaRepository<MemberGrade, Long> {
    Optional<MemberGrade> findByGradeLevel(int gradeLevel);

    Optional<MemberGrade> findFirstByRequiredRecipeCountLessThanEqualAndRequiredReviewCountLessThanEqualAndRequiredCommentCountLessThanEqualOrderByGradeLevelDesc(
            int recipeCount,
            int reviewCount,
            int commentCount
    );
}
