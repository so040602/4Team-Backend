package com.recipe.dto;

import lombok.*;
import lombok.Builder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDTO {
    private Long memberId;           // 회원 고유 ID
    private String displayName;      // 표시 이름
    private String primaryEmail;     // 주 이메일
    private String role;             // 권한
}
