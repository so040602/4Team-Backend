package com.recipe.Repository;

import com.recipe.entity.Member;
import com.recipe.entity.Recipe;
import com.recipe.entity.RecipeView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RecipeViewRepository extends JpaRepository<RecipeView, Long> {
    RecipeView findByRecipeAndMember(Recipe recipe, Member member);
    List<RecipeView> findByMemberOrderByViewedAtDesc(Member member);
    void deleteByRecipe(Recipe recipe);
}
