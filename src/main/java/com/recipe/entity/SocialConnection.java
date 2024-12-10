package com.recipe.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "social_connections",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"provider", "provider_id"})
        })
public class SocialConnection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SocialProvider provider;    // 소셜 로그인 제공자

    @Column(name = "provider_id", nullable = false)
    private String providerId;        // 소셜 서비스의 고유 ID

    @Column(name = "social_email")
    private String socialEmail;       // 소셜 계정의 이메일

    @Column(name = "social_name")
    private String socialName;        // 소셜 계정의 이름

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;

    // 편의 메소드: 소셜 연동 생성
    public static SocialConnection createSocialConnection(
            SocialProvider provider,
            String providerId,
            String email,
            String name) {
        return SocialConnection.builder()
                .provider(provider)
                .providerId(providerId)
                .socialEmail(email)
                .socialName(name)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SocialConnection)) return false;
        SocialConnection that = (SocialConnection) o;
        return provider == that.provider && providerId.equals(that.providerId);
    }

    @Override
    public int hashCode() {
        return 31 * provider.hashCode() + providerId.hashCode();
    }
}
