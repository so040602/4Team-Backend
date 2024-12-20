package com.recipe.Repository;

import com.recipe.entity.Member;
import com.recipe.entity.Recipe;
import com.recipe.entity.RecipeView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RecipeViewRepository extends JpaRepository<RecipeView, Long> {
    RecipeView findByRecipeAndMember(Recipe recipe, Member member);
    List<RecipeView> findByMemberOrderByViewedAtDesc(Member member);
}
