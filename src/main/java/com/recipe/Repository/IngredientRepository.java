package com.recipe.Repository;

import com.recipe.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    List<Ingredient> findByIngredientNameContainingIgnoreCase(String keyword);
}
