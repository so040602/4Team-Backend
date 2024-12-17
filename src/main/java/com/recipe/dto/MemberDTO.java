package com.recipe.dto;

import lombok.*;
import lombok.Builder;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDTO {
    private Long memberId;           // 회원 고유 ID
    private String primaryEmail;     // 주 이메일
    private String displayName;      // 표시 이름
    private String role;             // 권한
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isFollowing;

    @Builder
    public MemberDTO(Long memberId, String displayName, String primaryEmail, String role, boolean isFollowing, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.memberId = memberId;
        this.displayName = displayName;
        this.primaryEmail = primaryEmail;
        this.role = role;
        this.isFollowing = isFollowing;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
