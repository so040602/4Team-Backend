package com.recipe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeLikeResponseDTO {
    private boolean liked;    // 현재 좋아요 상태
    private int likeCount;    // 현재 좋아요 수
}
