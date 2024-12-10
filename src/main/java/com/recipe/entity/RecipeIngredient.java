package com.recipe.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipeIngredient_idx")
    private Long recipeIngredient_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_idx") // 외래키로 지정
    private Recipe recipe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_idx")
    private Ingredient ingredient;

    private String food_unit;
}
