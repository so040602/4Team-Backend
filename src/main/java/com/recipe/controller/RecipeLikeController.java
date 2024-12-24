package com.recipe.controller;

import com.recipe.dto.RecipeLikeResponseDTO;
import com.recipe.jwt.JWTUtil;
import com.recipe.service.RecipeLikeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recipe/like")
@RequiredArgsConstructor
public class RecipeLikeController {
    private final RecipeLikeService recipeLikeService;
    private final JWTUtil jwtUtil;

    @PostMapping("/{recipeId}")
    public ResponseEntity<RecipeLikeResponseDTO> toggleLike(
            @PathVariable Long recipeId,
            HttpServletRequest request
    ) {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            try {
                Long memberId = jwtUtil.getMemberId(token);
                RecipeLikeResponseDTO response = recipeLikeService.toggleLike(recipeId, memberId);
                return ResponseEntity.ok(response);
            } catch (Exception e) {
                return ResponseEntity.badRequest().build();
            }
        }
        return ResponseEntity.badRequest().build();
    }
}
