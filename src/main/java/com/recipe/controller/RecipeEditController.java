package com.recipe.controller;

import com.recipe.dto.RecipeCreateRequestDTO;
import com.recipe.dto.RecipeCreateResponseDTO;
import com.recipe.jwt.JWTUtil;
import com.recipe.service.RecipeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recipe/edit")
@RequiredArgsConstructor
public class RecipeEditController {
    private final RecipeService recipeService;
    private final JWTUtil jwtUtil;

    @PutMapping("/{recipeId}")
    public ResponseEntity<RecipeCreateResponseDTO> updateRecipe(
            @PathVariable Long recipeId,
            @Valid @RequestBody RecipeCreateRequestDTO requestDTO,
            HttpServletRequest request
    ) {
        String token = request.getHeader("Authorization").substring(7);
        Long memberId = jwtUtil.getMemberId(token);

        RecipeCreateResponseDTO responseDTO = recipeService.updateRecipe(recipeId, memberId, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    // 새로운 삭제 엔드포인트
    @DeleteMapping("/{recipeId}")
    public ResponseEntity<?> deleteRecipe(
            @PathVariable Long recipeId,
            HttpServletRequest request
    ) {
        String token = request.getHeader("Authorization").substring(7);
        Long memberId = jwtUtil.getMemberId(token);

        recipeService.deleteRecipe(recipeId, memberId);
        return ResponseEntity.ok().build();
    }
}
