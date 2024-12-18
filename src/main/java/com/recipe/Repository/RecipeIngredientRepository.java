package com.recipe.Repository;

import com.recipe.dto.RecipeDTO;
import com.recipe.entity.RecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {
    @Query("SELECT new com.recipe.dto.RecipeDTO(r.recipeId, r.recipeTitle, r.recipeTip, r.recipeThumbnail) " +
    "FROM Recipe r " +
    "JOIN RecipeIngredient ri ON r.recipeId = ri.recipe.id " +
    "WHERE ri.ingredient.id IN :ingredientIds " +
    "GROUP BY r.id " +
    "ORDER BY COUNT(ri.ingredient.id) DESC")
    List<RecipeDTO> findTopRecipesByIngredients(@Param("ingredientIds") List<Long> ingredientIds);
}
