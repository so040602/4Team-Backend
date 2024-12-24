package com.recipe.Repository;

import com.recipe.entity.Recipe;
import com.recipe.entity.RegistrationState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    Optional<Recipe> findByMember_MemberIdAndRegistrationState(Long memberId, RegistrationState registrationState);
    Optional<Recipe> findByRecipeIdAndMember_MemberId(Long recipeId, Long memberId);
    List<Recipe> findByRecipeTitleContainingIgnoreCase(String searchData);
    List<Recipe> findByRegistrationState(RegistrationState registrationState);
    List<Recipe> findAllByMember_MemberIdAndRegistrationState(Long memberId, RegistrationState registrationState);
    Long countByMember_MemberIdAndRegistrationState(Long memberId, RegistrationState registrationState);
    Optional<Recipe> findByRecipeIdAndRegistrationState(Long recipeId, RegistrationState registrationState);
}