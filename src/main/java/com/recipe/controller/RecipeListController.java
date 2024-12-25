package com.recipe.controller;

import com.recipe.dto.RecipeDTO;
import com.recipe.jwt.JWTUtil;
import com.recipe.service.RecipeListService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recipe_form")
@RequiredArgsConstructor
public class RecipeListController {
    private final RecipeListService recipeListService;
    private final JWTUtil jwtUtil;

    @GetMapping("/list")
    public ResponseEntity<List<RecipeDTO>> getAllRecipes(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        Long memberId = null;
        
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            try {
                memberId = jwtUtil.getMemberId(jwtToken);
            } catch (Exception e) {
                // 토큰이 유효하지 않은 경우 처리
            }
        }
        
        List<RecipeDTO> recipes = recipeListService.getAllRecipes(memberId);
        return ResponseEntity.ok(recipes);
    }
}
