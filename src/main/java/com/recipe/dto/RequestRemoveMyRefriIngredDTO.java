package com.recipe.dto;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestRemoveMyRefriIngredDTO {
    private Long refriId;
    private Long ingredientId;
}
