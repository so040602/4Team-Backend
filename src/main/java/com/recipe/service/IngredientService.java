package com.recipe.service;

import com.recipe.dto.IngredientSearchDTO;
import com.recipe.Repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class IngredientService {
    private final IngredientRepository ingredientRepository;

    public List<IngredientSearchDTO> searchIngredientsByKeyword(String keyword) {
        return ingredientRepository.findByIngredientNameContainingIgnoreCase(keyword).stream()
                .map(ingredient -> IngredientSearchDTO.builder()
                        .ingredientId(ingredient.getIngredientId())
                        .ingredientName(ingredient.getIngredientName())
                        .build())
                .collect(Collectors.toList());
    }
}
