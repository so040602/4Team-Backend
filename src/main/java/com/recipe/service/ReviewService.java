package com.recipe.service;

import com.recipe.Repository.ReviewRepository;
import com.recipe.dto.ReviewDTO;
import com.recipe.dto.ReviewCommentDTO;
import com.recipe.entity.Review;
import com.recipe.entity.ReviewComment;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public Page<ReviewDTO> getReviewList(Pageable pageable) {
        return reviewRepository.findAllByOrderByRevCreatedAtDesc(pageable)
                .map(this::convertToDTO);
    }

    @Transactional
    public ReviewDTO getReviewDetail(Long reviewId) {
        Review review = reviewRepository.findByIdWithMember(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        review.setRev_view_count(review.getRev_view_count() + 1);
        return convertToDTO(review);
    }

    @Transactional
    public ReviewDTO createReview(ReviewDTO reviewDTO) {
        Review review = Review.builder()
                .rev_title(reviewDTO.getRev_title())
                .rev_content(reviewDTO.getRev_content())
                .rev_image_url(reviewDTO.getRev_image_url())
                .rev_view_count(0)
                .build();

        return convertToDTO(reviewRepository.save(review));
    }

    @Transactional
    public ReviewDTO updateReview(Long reviewId, ReviewDTO reviewDTO) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        review.setRev_title(reviewDTO.getRev_title());
        review.setRev_content(reviewDTO.getRev_content());
        review.setRev_image_url(reviewDTO.getRev_image_url());

        return convertToDTO(review);
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    @Transactional
    public void toggleLike(Long reviewId) {
        // 좋아요 로직 구현
    }

    @Transactional
    public ReviewCommentDTO addComment(Long reviewId, ReviewCommentDTO commentDTO) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        ReviewComment comment = ReviewComment.builder()
                .rev_comment_content(commentDTO.getRev_comment_content())
                .build();

        // 댓글 저장 로직 구현
        return convertToCommentDTO(comment);
    }

    private ReviewDTO convertToDTO(Review review) {
        return ReviewDTO.builder()
                .rev_id(review.getRev_id())
                .rev_title(review.getRev_title())
                .rev_content(review.getRev_content())
                .rev_image_url(review.getRev_image_url())
                .rev_view_count(review.getRev_view_count())
                .rev_created_at(review.getRevCreatedAt())
                .rev_updated_at(review.getRev_updated_at())
                .memberName(review.getMember().getName())
                .likeCount(review.getReviewLikes().size())
                .comments(convertToCommentDTOList(review.getReviewComments()))
                .build();
    }

    private ReviewCommentDTO convertToCommentDTO(ReviewComment comment) {
        return ReviewCommentDTO.builder()
                .rev_comment_id(comment.getRev_comment_id())
                .rev_comment_content(comment.getRev_comment_content())
                .memberName(comment.getMember().getName())
                .rev_comment_created_at(comment.getRev_comment_created_at())
                .build();
    }

    private List<ReviewCommentDTO> convertToCommentDTOList(List<ReviewComment> comments) {
        List<ReviewCommentDTO> commentDTOs = new ArrayList<>();
        for (ReviewComment comment : comments) {
            commentDTOs.add(convertToCommentDTO(comment));
        }
        return commentDTOs;
    }
}