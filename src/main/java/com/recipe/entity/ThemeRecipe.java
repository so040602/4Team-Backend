package com.recipe.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "theme_recipe")
public class ThemeRecipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long themeRecipeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id")
    private Theme theme;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    private Integer displayOrder;
}
