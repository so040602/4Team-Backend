package com.recipe.controller;

import com.recipe.dto.IngredientSearchDTO;
import com.recipe.service.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/recipe_form")
@RequiredArgsConstructor
public class IngredientController {
    private final IngredientService ingredientService;

    @GetMapping("/searchingredient")
    public ResponseEntity<List<IngredientSearchDTO>> searchIngredients(@RequestParam String keyword) {
        List<IngredientSearchDTO> ingredients = ingredientService.searchIngredientsByKeyword(keyword);
        return ResponseEntity.ok().body(ingredients);
    }
}
