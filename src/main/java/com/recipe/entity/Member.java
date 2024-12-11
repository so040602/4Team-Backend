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
@Table(name = "members")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false)
    private String displayName;    // 서비스에서 표시될 이름

    @Column(unique = true, nullable = false)
    private String primaryEmail;   // 서비스에서 사용할 주 이메일 (로그인용)

    private String password;       // 로컬 로그인용 비밀번호

    @Column(nullable = false)
    private String role;           // 권한 (ROLE_USER, ROLE_ADMIN)

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SocialConnection> socialConnections = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Recipe> recipes = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<RecipeLike> recipeLikes = new ArrayList<>();

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;

    // 소셜 연동 추가 메소드
    public void addSocialConnection(SocialConnection socialConnection) {
        this.socialConnections.add(socialConnection);
        socialConnection.setMember(this);
    }

    // 소셜 연동 제거 메소드
    public void removeSocialConnection(SocialConnection socialConnection) {
        this.socialConnections.remove(socialConnection);
        socialConnection.setMember(null);
    }

    // 편의 메소드: 특정 제공자의 소셜 연동 찾기
    public SocialConnection findSocialConnection(SocialProvider provider) {
        return this.socialConnections.stream()
                .filter(conn -> conn.getProvider() == provider)
                .findFirst()
                .orElse(null);
    }

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Menu> menus;

}
