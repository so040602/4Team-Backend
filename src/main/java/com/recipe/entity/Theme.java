package com.recipe.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "theme")
public class Theme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long themeId;

    @Column(nullable = false)
    private String themeName;

    private String themeDescription;

    @Column(nullable = false)
    private Timestamp startDate;

    @Column(nullable = false)
    private Timestamp endDate;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;

    @OneToMany(mappedBy = "theme", cascade = CascadeType.ALL)
    private List<ThemeRecipe> themeRecipes = new ArrayList<>();
}
