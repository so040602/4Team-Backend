package com.recipe.service;

import com.recipe.Repository.RecipeRepository;
import com.recipe.Repository.RecipeLikeRepository;
import com.recipe.dto.RecipeDTO;
import com.recipe.entity.Recipe;
import com.recipe.entity.RegistrationState;
import com.recipe.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeListService {
    private final RecipeRepository recipeRepository;
    private final RecipeLikeRepository recipeLikeRepository;

    public List<RecipeDTO> getAllRecipes(Long currentUserId) {
        List<Recipe> recipes = recipeRepository.findByRegistrationState(RegistrationState.PUBLISHED);
        return recipes.stream()
                .map(recipe -> convertToDTO(recipe, currentUserId))
                .collect(Collectors.toList());
    }

    private RecipeDTO convertToDTO(Recipe recipe, Long currentUserId) {
        RecipeDTO dto = new RecipeDTO();
        dto.setRecipeId(recipe.getRecipeId());
        dto.setRecipeTitle(recipe.getRecipeTitle());
        dto.setRecipeThumbnail(recipe.getRecipeThumbnail());
        dto.setRecipeTip(recipe.getRecipeTip());
        
        Member member = recipe.getMember();
        if (member != null) {
            dto.setDisplayName(member.getDisplayName());
            dto.setMemberId(member.getMemberId());
        }
        
        dto.setLikeCount(recipe.getLikeCount());
        
        // 현재 사용자가 이 레시피를 좋아요 했는지 확인
        if (currentUserId != null) {
            boolean isLiked = recipeLikeRepository.findByRecipe_RecipeIdAndMember_MemberId(
                recipe.getRecipeId(), currentUserId).isPresent();
            dto.setLiked(isLiked);
        }
        
        return dto;
    }
}
