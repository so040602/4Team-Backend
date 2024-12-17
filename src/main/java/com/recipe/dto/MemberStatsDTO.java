package com.recipe.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberStatsDTO {
    private Long memberId;
    private Long recipeCount;
    private Long reviewCount;
    private Long followerCount;
    private Long followingCount;
}
