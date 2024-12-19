package com.recipe.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDTO {
    private Long id;
    private String title;
    private String content;
    private String imageUrl;
    private Integer rating;
    private Long memberId;
    private String memberDisplayName;
    private Long recipeId;
    private String recipeTitle;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer viewCount;
}