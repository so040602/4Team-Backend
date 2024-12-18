package com.recipe.dto;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeDTO {
    private Long recipeId;
    private String recipeTitle;
    private String recipeTip;
    private String recipeThumbnail;
}
