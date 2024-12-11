package com.recipe.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "cooking_tool")
public class CookingTool {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cookingToolId;

    @Column(nullable = false)
    private String cookingToolName;

    @OneToMany(mappedBy = "cookingTool")
    private List<RecipeCookingTool> recipeCookingTools = new ArrayList<>();
}
