package com.recipe.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyRefrigeratorIngredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long refriIngreId;

    @CreationTimestamp
    private Date regdate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "refri_id")
    private MyRefrigerator myRefrigerator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;
}
