package com.recipe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rev_id;

    @Column(nullable = false, length = 200)
    private String rev_title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String rev_content;

    private String rev_image_url;

    private Integer rev_view_count = 0;

    @CreationTimestamp
    private Timestamp revCreatedAt; // rev_created_at에서 revCreatedAt으로 변경

    @UpdateTimestamp
    private Timestamp rev_updated_at;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    private List<ReviewLike> reviewLikes = new ArrayList<>();

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    private List<ReviewComment> reviewComments = new ArrayList<>();
}