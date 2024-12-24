package com.recipe.Repository;

import com.recipe.entity.Recipe;
import com.recipe.entity.RecipeStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeStepRepository extends JpaRepository<RecipeStep, Long> {
    List<RecipeStep> findByRecipeOrderByStepOrder(Recipe recipe);

    // 특정 레시피의 모든 단계 삭제
    void deleteByRecipe(Recipe recipe);
}
