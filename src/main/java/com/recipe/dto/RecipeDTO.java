package com.recipe.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeDTO {
    private Long recipeId;
    private String recipeTitle;
    private String recipeTip;
    private String recipeThumbnail;
    private Long memberId;


    // 생성자 추가
    public RecipeDTO(Long recipeId, String recipeTitle, String recipeTip, String recipeThumbnail) {
        this.recipeId = recipeId;
        this.recipeTitle = recipeTitle;
        this.recipeTip = recipeTip;
        this.recipeThumbnail = recipeThumbnail;
    }
}
