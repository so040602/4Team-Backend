package com.recipe.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeTempSaveRequestDTO {
    @NotNull(message = "회원 ID는 필수입니다")
    private Long memberId;

    private String recipeTitle;
    private String recipeThumbnail;
    private String servingSize;
    private String cookingTime;
    private String difficultyLevel;
    private String situation;
    private String recipeTip;

    @Valid
    private List<RecipeStepDTO> recipeSteps;

    @Valid
    private List<RecipeIngredientDTO> recipeIngredients;

    @Valid
    private List<RecipeCookingToolDTO> recipeCookingTools;
}
