package com.recipe.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageRecipeDTO {
    private List<RecipeDTO> recipes;
    private int currentPage;
    private int totalPages;
    private long totalItems;
    private int itemsPerPage;
}
