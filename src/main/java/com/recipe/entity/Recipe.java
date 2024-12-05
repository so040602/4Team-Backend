package com.recipe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Recipe {
    @Id
    @Column(name = "recipe_idx")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long recipe_idx;

    private String name;
    private String food_level;
    private int time;
    private int step_order;
    private String cook_instruction;
    private String complete_image;
    private String tip;
    private int views;

    private LocalDateTime recipeDate;

    @OneToMany(mappedBy = "recipe", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<RecipeIngredient> ingredients = new ArrayList<>();

}
