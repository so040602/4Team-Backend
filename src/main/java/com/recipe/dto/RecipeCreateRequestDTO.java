package com.recipe.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeCreateRequestDTO {
    @NotNull(message = "회원 ID는 필수입니다")
    private Long memberId;

    @NotBlank(message = "레시피 제목은 필수입니다")
    @Size(min = 2, max = 20, message = "레시피 제목은 2자 이상 20자 이하여야 합니다")
    private String recipeTitle;

    @NotBlank(message = "레시피 썸네일은 필수입니다")
    private String recipeThumbnail;

    @NotBlank(message = "몇 인분인지 선택해주세요")
    private String servingSize;

    @NotBlank(message = "조리 시간은 필수입니다")
    private String cookingTime;

    @NotBlank(message = "난이도는 필수입니다")
    private String difficultyLevel;

    @NotBlank(message = "상황은 필수입니다")
    private String situation;

    @Size(max = 500, message = "조리 팁은 500자를 초과할 수 없습니다")
    private String recipeTip;

    @NotEmpty(message = "최소 하나의 조리 단계가 필요합니다")
    @Valid
    private List<RecipeStepDTO> recipeSteps;

    @NotEmpty(message = "최소 하나의 재료가 필요합니다")
    @Valid
    private List<RecipeIngredientDTO> recipeIngredients;

    @NotEmpty(message = "최소 하나의 조리도구가 필요합니다")
    @Valid
    private List<RecipeCookingToolDTO> recipeCookingTools;
}
