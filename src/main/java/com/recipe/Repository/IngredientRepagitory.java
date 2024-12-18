package com.recipe.Repository;

import com.recipe.dto.IngredientDTO;
import com.recipe.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IngredientRepagitory extends JpaRepository<Ingredient, Long> {
    @Query("SELECT new com.recipe.dto.IngredientDTO(i.ingredientId, i.ingredientName, i.ingredientImage) " + "FROM Ingredient i")
    List<IngredientDTO> findAllData();
}
