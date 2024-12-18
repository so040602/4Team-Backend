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
public class MyRefrigerator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long refriId;

    @CreationTimestamp
    private LocalDateTime regdate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "myRefrigerator", cascade = CascadeType.ALL)
    private List<MyRefrigeratorIngredient> myRefrigeratorIngredients = new ArrayList<>();
}
