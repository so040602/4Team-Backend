package com.recipe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ingredient {
    @Id
    @Column(name = "Ingredient_idx")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long ingredient_idx;


    private String ingredient;
    private String image;


}
