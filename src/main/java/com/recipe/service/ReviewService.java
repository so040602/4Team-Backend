package com.recipe.service;

import com.recipe.Repository.MemberRepository;
import com.recipe.Repository.ReviewCommentRepository;
import com.recipe.Repository.ReviewRepository;
import com.recipe.dto.ReviewDTO;
import com.recipe.dto.ReviewCommentDTO;
import com.recipe.entity.Review;
import com.recipe.entity.ReviewComment;
import com.recipe.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewCommentRepository reviewCommentRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ReviewDTO createReview(ReviewDTO reviewDTO) {
        Member member = memberRepository.findById(reviewDTO.getMemberId())
                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));

        Review review = Review.builder()
                .title(reviewDTO.getTitle())
                .content(reviewDTO.getContent())
                .imageUrl(reviewDTO.getImageUrl()) // imageUrls 대신 imageUrl 사용
                .rating(reviewDTO.getRating())
                .member(member)
                .build();

        Review savedReview = reviewRepository.save(review);
        return convertToDTO(savedReview);
    }

    @Transactional(readOnly = true)
    public ReviewDTO getReviewById(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        return convertToDTO(review);
    }

    @Transactional(readOnly = true)
    public List<ReviewDTO> getAllReviews() {
        return reviewRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ReviewDTO updateReview(Long id, ReviewDTO reviewDTO) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        review.setTitle(reviewDTO.getTitle());
        review.setContent(reviewDTO.getContent());
        review.setImageUrl(reviewDTO.getImageUrl());
        review.setRating(reviewDTO.getRating());

        return convertToDTO(reviewRepository.save(review));
    }

    @Transactional
    public void deleteReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        reviewRepository.delete(review);
    }

    @Transactional
    public ReviewCommentDTO addComment(ReviewCommentDTO commentDTO) {
        Review review = reviewRepository.findById(commentDTO.getReviewId())
                .orElseThrow(() -> new RuntimeException("Review not found"));
        Member member = memberRepository.findById(commentDTO.getMemberId())
                .orElseThrow(() -> new RuntimeException("Member not found"));

        ReviewComment parentComment = null;
        if (commentDTO.getParentId() != null) {
            parentComment = reviewCommentRepository.findById(commentDTO.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent comment not found"));
        }

        ReviewComment comment = ReviewComment.builder()
                .content(commentDTO.getContent())
                .review(review)
                .member(member)
                .parent(parentComment)
                .build();

        ReviewComment savedComment = reviewCommentRepository.save(comment);
        return convertToCommentDTO(savedComment);
    }

    private ReviewDTO convertToDTO(Review review) {
        return ReviewDTO.builder()
                .id(review.getId())
                .title(review.getTitle())
                .content(review.getContent())
                .imageUrl(review.getImageUrl())
                .rating(review.getRating())
                .viewCount(review.getViewCount())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .memberId(review.getMember().getMemberId())
                .build();
    }

    private ReviewCommentDTO convertToCommentDTO(ReviewComment comment) {
        return ReviewCommentDTO.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .reviewId(comment.getReview().getId())
                .memberId(comment.getMember().getMemberId())
                .parentId(comment.getParent() != null ? comment.getParent().getId() : null)
                .isDeleted(comment.isDeleted())
                .build();
    }
}