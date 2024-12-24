package com.recipe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeCardDTO {
    private Long recipeId;
    private String recipeTitle;
    private String recipeThumbnail;
    private String cookingTime;
    private String difficultyLevel;
    private String situation;
    private int likeCount;
    private Boolean liked;
    private List<String> cookingTools;
    private Timestamp createdAt;
    private Long themeId;
    private String themeName;
    private String themeDescription;
}
