package com.recipe.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FollowDTO {
    private Long id;
    private MemberDTO follower;
    private MemberDTO following;
}