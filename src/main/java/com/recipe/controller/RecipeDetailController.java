package com.recipe.controller;

import com.recipe.dto.RecipeDetailResponseDTO;
import com.recipe.jwt.JWTUtil;
import com.recipe.service.RecipeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recipe/detail")
public class RecipeDetailController {
    private final RecipeService recipeService;
    private final JWTUtil jwtUtil;


    @GetMapping("/{recipeId}")
    public ResponseEntity<RecipeDetailResponseDTO> getRecipeDetail(
            @PathVariable Long recipeId,
            HttpServletRequest request
    ) {
        Long currentUserId = null;
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            try {
                currentUserId = jwtUtil.getMemberId(token);
            } catch (Exception e) {
                // 토큰 파싱 실패 시 null 유지
            }
        }

        RecipeDetailResponseDTO recipe = recipeService.getRecipeDetail(recipeId, currentUserId);
        return ResponseEntity.ok(recipe);
    }
}
