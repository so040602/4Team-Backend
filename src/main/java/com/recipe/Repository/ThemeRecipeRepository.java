package com.recipe.Repository;

import com.recipe.entity.ThemeRecipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThemeRecipeRepository extends JpaRepository<ThemeRecipe, Long>{
    @Query("SELECT tr FROM ThemeRecipe tr " +
            "JOIN FETCH tr.recipe r " +
            "LEFT JOIN FETCH r.recipeCookingTools rct " +
            "LEFT JOIN FETCH rct.cookingTool " +
            "WHERE tr.theme.themeId = :themeId " +
            "ORDER BY tr.displayOrder")
    List<ThemeRecipe> findByThemeIdOrderByDisplayOrder(@Param("themeId") Long themeId);
}
