package com.recipe.Repository;

import com.recipe.entity.Recipe;
import com.recipe.entity.RecipeCookingTool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeCookingToolRepository extends JpaRepository<RecipeCookingTool, Long> {
    List<RecipeCookingTool> findByRecipe(Recipe recipe);
}
