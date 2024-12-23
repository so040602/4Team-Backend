package com.recipe.controller;

import com.recipe.dto.RecipeCreateRequestDTO;
import com.recipe.dto.RecipeCreateResponseDTO;
import com.recipe.dto.RecipeDTO;
import com.recipe.dto.RecipeTempSaveRequestDTO;
import com.recipe.jwt.JWTUtil;
import com.recipe.service.RecipeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("recipe_form")
@RequiredArgsConstructor
public class RecipeController {
    private final RecipeService recipeService;
    private final JWTUtil jwtUtil;

    @GetMapping("/temp-saved/{memberId}")
    public ResponseEntity<RecipeCreateResponseDTO> getTempSavedRecipe(@PathVariable Long memberId) {
        RecipeCreateResponseDTO recipe = recipeService.getTempSavedRecipe(memberId);
        if (recipe == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(recipe);
    }

    @PostMapping("/create")
    public ResponseEntity<RecipeCreateResponseDTO> createRecipe(@Valid @RequestBody RecipeCreateRequestDTO requestDTO) {
        RecipeCreateResponseDTO responseDTO = recipeService.createRecipe(requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/temp-save")
    public ResponseEntity<RecipeCreateResponseDTO> tempSaveRecipe(@Valid @RequestBody RecipeTempSaveRequestDTO requestDTO) {
        RecipeCreateResponseDTO responseDTO = recipeService.tempSaveRecipe(requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/recipes")
    public ResponseEntity<List<RecipeDTO>> getAllRecipes() {
        List<RecipeDTO> recipes = recipeService.getAllRecipes();
        return ResponseEntity.ok(recipes);
    }
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<RecipeDTO>> getRecipesByMemberId(@PathVariable Long memberId) {
        List<RecipeDTO> recipes = recipeService.getRecipesByMemberId(memberId);
        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/member/{memberId}/count")
    public ResponseEntity<Long> getRecipeCount(@PathVariable Long memberId) {
        Long count = recipeService.getRecipeCount(memberId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/recent")
    public ResponseEntity<List<RecipeDTO>> getRecentViews(
            @RequestHeader("Authorization") String token) {
        String jwtToken = token.substring(7); // "Bearer " 제거
        Long memberId = jwtUtil.getMemberId(jwtToken);
        return ResponseEntity.ok(recipeService.getRecentViewsByMemberId(memberId));
    }

    @PostMapping("/{recipeId}/view")
    public ResponseEntity<Void> addRecipeView(
            @PathVariable Long recipeId,
            @RequestHeader("Authorization") String token) {
        String jwtToken = token.substring(7); // "Bearer " 제거
        Long memberId = jwtUtil.getMemberId(jwtToken);
        recipeService.addRecipeView(recipeId, memberId);
        return ResponseEntity.ok().build();
    }
}