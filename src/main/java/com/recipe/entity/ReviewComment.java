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
@Table(name = "review_comment")
public class ReviewComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rev_comment_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rev_id")
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String rev_comment_content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rev_comment_parent_id")
    private ReviewComment parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<ReviewComment> children = new ArrayList<>();

    @CreationTimestamp
    private Timestamp rev_comment_created_at;

    @UpdateTimestamp
    private Timestamp rev_comment_updated_at;
}