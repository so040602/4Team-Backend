package com.recipe.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeStepDTO {
    @Min(value = 1, message = "조리 순서는 1 이상이어야 합니다")
    private Integer stepOrder;

    @Size(max = 1000, message = "조리 설명은 1000자를 초과할 수 없습니다")
    private String stepDescription;

    @NotBlank(message = "조리 과정 사진은 필수입니다")
    private String stepImage;
}
