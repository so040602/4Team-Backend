package com.recipe.service;

import com.recipe.dto.CookingToolDTO;
import com.recipe.Repository.CookingToolRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CookingToolService {
    private final CookingToolRepository cookingToolRepository;

    public List<CookingToolDTO> getAllCookingTools() {
        return cookingToolRepository.findAll().stream()
                .map((tool) -> CookingToolDTO.builder()
                        .cookingId(tool.getCookingToolId())
                        .cookingToolName(tool.getCookingToolName())
                        .build())
                .collect(Collectors.toList());
    }
}
