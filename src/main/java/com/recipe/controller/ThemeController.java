package com.recipe.controller;

import com.recipe.dto.RecipeCardDTO;
import com.recipe.jwt.JWTUtil;
import com.recipe.service.ThemeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/themes")
@RequiredArgsConstructor
public class ThemeController {
    private final ThemeService themeService;
    private final JWTUtil jwtUtil;

    @GetMapping("/{themeId}/recipes")
    public ResponseEntity<List<RecipeCardDTO>> getThemeRecipes(
            @PathVariable Long themeId,
            HttpServletRequest request) {

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

        List<RecipeCardDTO> recipes = themeService.getRecipesByThemeId(themeId, currentUserId);
        return ResponseEntity.ok(recipes);
    }
}
