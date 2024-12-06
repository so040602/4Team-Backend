package com.recipe.entity;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

@Data
@Entity
@Table(name = "Menu")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_idx")
    private Long menuIdx; // 메뉴 고유 식별자 (Primary Key)

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    @JsonIgnore
    private Member member;; // 회원 ID (외래키)

    @Column(name = "name", nullable = false, length = 255)
    private String name; // 메뉴 이름

    @Column(name = "description", length = 255)
    private String description; // 메뉴 설명

    @Column(name = "image", length = 255)
    private String image; // 이미지 파일 경로

    @Column(name = "season", length = 255)
    private String season; // 메뉴와 연관된 계절 (봄/여름/가을/겨울)

    @Column(name = "meal_time", length = 255)
    private String mealTime; // 시간대 (아침/점심/저녁)

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", updatable = false)
    private Date createdAt; // 생성 날짜

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt; // 수정 날짜

    // JPA의 콜백 메서드로 날짜 자동 처리
    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Date();
    }
}
