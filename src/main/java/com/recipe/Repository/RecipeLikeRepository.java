package com.recipe.Repository;

import com.recipe.entity.Recipe;
import com.recipe.entity.RecipeLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeLikeRepository extends JpaRepository<RecipeLike, Long> {
    // 특정 레시피에 대한 특정 사용자의 좋아요 여부 확인
    boolean existsByRecipe_RecipeIdAndMember_MemberId(Long recipeId, Long memberId);

    // 특정 레시피의 좋아요 삭제 (좋아요 취소 시 사용)
    void deleteByRecipe_RecipeIdAndMember_MemberId(Long recipeId, Long memberId);

    // 특정 레시피의 좋아요 수 조회
    long countByRecipe_RecipeId(Long recipeId);

    // 특정 레시피의 모든 좋아요 삭제
    void deleteByRecipe(Recipe recipe);

    List<RecipeLike> findByMember_MemberId(Long memberId);

    Optional<RecipeLike> findByRecipe_RecipeIdAndMember_MemberId(Long recipeId, Long memberId);
}
