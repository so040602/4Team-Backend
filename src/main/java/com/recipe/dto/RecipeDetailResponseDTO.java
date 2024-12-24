package com.recipe.dto;

import com.recipe.entity.RegistrationState;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Builder
public class RecipeDetailResponseDTO {
    private Long recipeId;
    private Long memberId;
    private String displayName;
    private String recipeTitle;
    private String recipeThumbnail;
    private String servingSize;
    private String cookingTime;
    private String difficultyLevel;
    private String situation;
    private String recipeTip;
    private RegistrationState registrationState;
    private Timestamp createdAt;

    private int likeCount;
    private boolean isLiked;

    private List<RecipeStepDTO> recipeSteps;
    private List<RecipeIngredientDTO> recipeIngredients;
    private List<RecipeCookingToolDTO> recipeCookingTools;

    @Getter
    @Builder
    public static class RecipeCookingToolDTO {
        private Long cookingToolId;
        private String cookingToolName;
    }
}
