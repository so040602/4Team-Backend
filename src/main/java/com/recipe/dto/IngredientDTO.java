package com.recipe.dto;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IngredientDTO {
    private Long ingredientId;
    private String ingredientName;
    private String ingredientImage;
}
