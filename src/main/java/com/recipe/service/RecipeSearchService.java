package com.recipe.service;

import com.recipe.Repository.RecipeRepository;
import com.recipe.dto.RecipeDTO;
import com.recipe.entity.Recipe;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeSearchService {
    private final RecipeRepository recipeRepository;

    public List<RecipeDTO> searchRecipeService(String searchData) {
        List<Recipe> recipes = recipeRepository.findByRecipeTitleContainingIgnoreCase(searchData);

        return recipes.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private RecipeDTO convertToDTO(Recipe recipe){
        RecipeDTO recipeDTO = new RecipeDTO();
        recipeDTO.setRecipeId(recipe.getRecipeId());
        recipeDTO.setRecipeTitle(recipe.getRecipeTitle());
        recipeDTO.setRecipeThumbnail(recipe.getRecipeThumbnail());
        recipeDTO.setRecipeTip(recipe.getRecipeTip());

        return recipeDTO;
    }
}
