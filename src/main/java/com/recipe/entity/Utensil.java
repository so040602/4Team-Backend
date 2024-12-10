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
public class Utensil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "utensil_id")
    private Long utensil_id;

    private String utensil;
}
