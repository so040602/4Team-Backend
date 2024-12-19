package com.recipe.Repository;

import com.recipe.entity.Recipe;
import com.recipe.entity.RegistrationState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    Optional<Recipe> findByMember_MemberIdAndRegistrationState(Long memberId, RegistrationState registrationState);
    Optional<Recipe> findByRecipeIdAndMember_MemberId(Long recipeId, Long memberId);
}
