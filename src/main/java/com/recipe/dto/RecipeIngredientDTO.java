package com.recipe.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeIngredientDTO {
    private Long ingredientId;
    private String ingredientName;

    @NotBlank(message = "재료의 양은 필수입니다")
    @Size(max = 10, message = "재료의 양은 10자를 초과할 수 없습니다")
    private String ingredientAmount;
}
