package com.recipe.service;

import com.recipe.Repository.RecipeLikeRepository;
import com.recipe.Repository.ThemeRecipeRepository;
import com.recipe.dto.RecipeCardDTO;
import com.recipe.entity.Recipe;
import com.recipe.entity.Theme;
import com.recipe.entity.ThemeRecipe;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ThemeService {
    private final ThemeRecipeRepository themeRecipeRepository;
    private final RecipeLikeRepository recipeLikeRepository;

    public List<RecipeCardDTO> getRecipesByThemeId(Long themeId, Long memberId) {
        List<ThemeRecipe> themeRecipes = themeRecipeRepository.findByThemeIdOrderByDisplayOrder(themeId);
        Set<Long> likedRecipeIds = getLikedRecipeIds(memberId);

        return themeRecipes.stream()
                .map(themeRecipe -> convertToRecipeCardDTO(themeRecipe, likedRecipeIds))
                .collect(Collectors.toList());
    }

    private Set<Long> getLikedRecipeIds(Long memberId) {
        if (memberId == null) {
            return new HashSet<>();
        }
        return recipeLikeRepository.findByMember_MemberId(memberId).stream()
                .map(recipeLike -> recipeLike.getRecipe().getRecipeId())
                .collect(Collectors.toSet());
    }

    private RecipeCardDTO convertToRecipeCardDTO(ThemeRecipe themeRecipe, Set<Long> likedRecipeIds) {
        Recipe recipe = themeRecipe.getRecipe();
        Theme theme = themeRecipe.getTheme();

        List<String> cookingTools = recipe.getRecipeCookingTools().stream()
                .map(tool -> tool.getCookingTool().getCookingToolName())
                .collect(Collectors.toList());

        return RecipeCardDTO.builder()
                .recipeId(recipe.getRecipeId())
                .recipeTitle(recipe.getRecipeTitle())
                .recipeThumbnail(recipe.getRecipeThumbnail())
                .cookingTime(recipe.getCookingTime())
                .difficultyLevel(recipe.getDifficultyLevel())
                .situation(recipe.getSituation())
                .likeCount(recipe.getLikeCount())
                .liked(likedRecipeIds.contains(recipe.getRecipeId()))
                .cookingTools(cookingTools)
                .createdAt(recipe.getCreatedAt())
                .themeId(theme.getThemeId())
                .themeName(theme.getThemeName())
                .themeDescription(theme.getThemeDescription())
                .build();
    }
}
