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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewCommentRepository reviewCommentRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ReviewDTO createReview(ReviewDTO reviewDTO, MultipartFile image) {
        try {
            String imageUrl = saveImage(image);
            reviewDTO.setImageUrl(imageUrl);
            Member member = memberRepository.findById(reviewDTO.getMemberId())
                    .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));

            Review review = Review.builder()
                    .title(reviewDTO.getTitle())
                    .content(reviewDTO.getContent())
                    .imageUrl(imageUrl)
                    .rating(reviewDTO.getRating())
                    .member(member)
                    .build();

            Review savedReview = reviewRepository.save(review);
            return convertToDTO(savedReview);
        } catch (IOException e) {
            throw new RuntimeException("이미지 저장에 실패했습니다", e);
        }
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
    public ReviewDTO updateReview(Long id, ReviewDTO reviewDTO, MultipartFile image) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("리뷰를 찾을 수 없습니다"));

        // 권한 체크
        if (!review.getMember().getMemberId().equals(reviewDTO.getMemberId())) {
            throw new RuntimeException("리뷰 수정 권한이 없습니다");
        }

        try {
            String imageUrl = image != null ? saveImage(image) : review.getImageUrl();

            review.setTitle(reviewDTO.getTitle());
            review.setContent(reviewDTO.getContent());
            review.setImageUrl(imageUrl);
            review.setRating(reviewDTO.getRating());

            Review updatedReview = reviewRepository.save(review);
            return convertToDTO(updatedReview);
        } catch (IOException e) {
            throw new RuntimeException("이미지 저장에 실패했습니다", e);
        }
    }

    @Transactional
    public void deleteReview(Long id, Long memberId) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        if (!review.getMember().getMemberId().equals(memberId)) {
            throw new RuntimeException("You are not authorized to delete this review");
        }
        reviewRepository.delete(review);
    }

    @Transactional
    public ReviewCommentDTO addComment(ReviewCommentDTO commentDTO) {
        Review review = reviewRepository.findById(commentDTO.getReviewId())
                .orElseThrow(() -> new RuntimeException("리뷰를 찾을 수 없습니다."));

        ReviewComment comment = ReviewComment.builder()
                .content(commentDTO.getContent())
                .member(memberRepository.findById(commentDTO.getMemberId())
                        .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다.")))
                .review(review)
                .build();

        ReviewComment savedComment = reviewCommentRepository.save(comment);
        return convertToCommentDTO(savedComment);
    }

    @Transactional
    public ReviewCommentDTO addReply(ReviewCommentDTO commentDTO) {
        ReviewComment parentComment = reviewCommentRepository.findById(commentDTO.getParentId())
                .orElseThrow(() -> new RuntimeException("부모 댓글을 찾을 수 없습니다."));

        Review review = reviewRepository.findById(parentComment.getReview().getId())
                .orElseThrow(() -> new RuntimeException("리뷰를 찾을 수 없습니다."));

        ReviewComment reply = ReviewComment.builder()
                .content(commentDTO.getContent())
                .member(memberRepository.findById(commentDTO.getMemberId())
                        .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다.")))
                .review(review)
                .parent(parentComment)
                .build();

        ReviewComment savedReply = reviewCommentRepository.save(reply);
        return convertToCommentDTO(savedReply);
    }

    @Transactional(readOnly = true)
    public List<ReviewCommentDTO> getCommentsByReviewId(Long reviewId) {
        List<ReviewComment> comments = reviewRepository.findCommentsByReviewId(reviewId);
        return comments.stream()
                .map(this::convertToCommentDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ReviewCommentDTO updateComment(Long id, ReviewCommentDTO commentDTO) {
        ReviewComment comment = reviewCommentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));

        // 작성자 확인 (실제 구현에서는 보안 로직 추가 필요)
        if (!comment.getMember().getMemberId().equals(commentDTO.getMemberId())) {
            throw new RuntimeException("댓글 수정 권한이 없습니다.");
        }

        comment.setContent(commentDTO.getContent());
        ReviewComment updatedComment = reviewCommentRepository.save(comment);
        return convertToCommentDTO(updatedComment);
    }

    @Transactional
    public void deleteComment(Long id, Long memberId) {
        ReviewComment comment = reviewCommentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        if (!comment.getMember().getMemberId().equals(memberId)) {
            throw new RuntimeException("You are not authorized to delete this comment");
        }
        reviewCommentRepository.delete(comment);
    }

    private String saveImage(MultipartFile image) throws IOException {
        if (image == null || image.isEmpty()) {
            return null;
        }

        String fileName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
        Path uploadPath = Paths.get("uploads");

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);
        Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return fileName;
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
                .memberDisplayName(review.getMember().getDisplayName())
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
                .memberDisplayName(comment.getMember().getDisplayName())
                .build();
    }
}