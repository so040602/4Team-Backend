package com.recipe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class MemberGrade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String gradeName;  // 등급 이름 (예: 새싹, 요리사, 셰프 등)

    private int requiredRecipeCount;    // 필요한 레시피 수
    private int requiredReviewCount;    // 필요한 리뷰 수
    private int requiredCommentCount;   // 필요한 댓글 수

    @Column(nullable = false)
    private int gradeLevel;  // 등급 레벨 (1: 새싹, 2: 요리사, 3: 셰프 등)
}
