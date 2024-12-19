package com.recipe.controller;

import com.recipe.dto.CookingToolDTO;
import com.recipe.service.CookingToolService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/recipe_form")
@RequiredArgsConstructor
public class CookingToolController {
    private final CookingToolService cookingToolService;

    @GetMapping("/getcookingtool")
    public ResponseEntity<List<CookingToolDTO>> getAllCookingTools() {
        List<CookingToolDTO> tools = cookingToolService.getAllCookingTools();

        return ResponseEntity.ok().body(tools);
    }
}
