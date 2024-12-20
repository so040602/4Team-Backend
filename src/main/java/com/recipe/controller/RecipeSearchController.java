package com.recipe.controller;

import com.recipe.dto.RecipeDTO;
import com.recipe.service.RecipeSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/recipe")
@RequiredArgsConstructor
public class RecipeSearchController {
    private final RecipeSearchService recipeService;

    @GetMapping("/searchrecipe/{searchData}")
    public ResponseEntity<List<RecipeDTO>> searchRecipe(@PathVariable String searchData){
        List<RecipeDTO> recipeDTOList =  recipeService.searchRecipeService(searchData);
        System.out.println(searchData);
        System.out.println(recipeDTOList);

        return ResponseEntity.ok(recipeDTOList);
    }
}
