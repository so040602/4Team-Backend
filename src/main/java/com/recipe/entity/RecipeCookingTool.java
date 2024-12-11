package com.recipe.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "recipe_cooking_tool")
public class RecipeCookingTool {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recipeCookingToolId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cooking_tool_id")
    private CookingTool cookingTool;
}
