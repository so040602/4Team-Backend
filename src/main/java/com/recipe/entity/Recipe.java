package com.recipe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recipe_idx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id") //외래키 컬럼
    private Member member;

    private String name;
    private String food_level;
    private int time;
    private String complete_image;
    private String tip;
    private int views;

    private String situation;

    private String state_recipe;

    @CreationTimestamp
    @Column(name = "recipe_date")
    private LocalDateTime recipeDate;

    @OneToMany(mappedBy = "recipe", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<RecipeIngredient> ingredients = new ArrayList<>();

    @OneToMany(mappedBy = "recipe", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<RecipeStep> recipeSteps = new ArrayList<>();

    @OneToMany(mappedBy = "recipe", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<RecipeUtensil> recipeUtensils = new ArrayList<>();
}
