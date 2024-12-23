package com.recipe.service;

import com.recipe.Repository.MemberRepository;
import com.recipe.Repository.ReviewCommentRepository;
import com.recipe.Repository.ReviewRepository;
import com.recipe.Repository.ReviewViewRepository;
import com.recipe.Repository.RecipeRepository;
import com.recipe.dto.ReviewDTO;
import com.recipe.dto.ReviewCommentDTO;
import com.recipe.entity.Review;
import com.recipe.entity.ReviewComment;
import com.recipe.entity.ReviewView;
import com.recipe.entity.Member;
import com.recipe.entity.Recipe;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewCommentRepository reviewCommentRepository;
    private final MemberRepository memberRepository;
    private final ReviewViewRepository reviewViewRepository;
    private final RecipeRepository recipeRepository;

    @Transactional
    public ReviewDTO createReview(ReviewDTO reviewDTO, MultipartFile image) {
        try {
            String imageUrl = saveImage(image);
            reviewDTO.setImageUrl(imageUrl);
            Member member = memberRepository.findById(reviewDTO.getMemberId())
                    .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));
            Recipe recipe = recipeRepository.findById(reviewDTO.getRecipeId())
                    .orElseThrow(() -> new RuntimeException("레시피를 찾을 수 없습니다."));

            Review review = Review.builder()
                    .title(reviewDTO.getTitle())
                    .content(reviewDTO.getContent())
                    .imageUrl(imageUrl)
                    .rating(reviewDTO.getRating())
                    .member(member)
                    .recipe(recipe)
                    .build();

            Review savedReview = reviewRepository.save(review);
            return convertToDTO(savedReview);
        } catch (IOException e) {
            throw new RuntimeException("이미지 저장에 실패했습니다", e);
        }
    }

    @Transactional
    public ReviewDTO getReviewById(Long id, Long memberId) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        // 로그인한 사용자인 경우에만 조회수 처리
        if (memberId != null) {
            addReviewView(id, memberId);
        }

        return convertToDTO(review);
    }

    @Transactional
    public void addReviewView(Long reviewId, Long memberId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("리뷰를 찾을 수 없습니다."));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));

        ReviewView existingView = reviewViewRepository.findByReviewAndMember(review, member);
        if (existingView != null) {
            existingView.setViewedAt(LocalDateTime.now());
            reviewViewRepository.save(existingView);
        } else {
            ReviewView reviewView = ReviewView.builder()
                    .review(review)
                    .member(member)
                    .build();
            reviewViewRepository.save(reviewView);
            review.setViewCount(review.getViewCount() + 1);
            reviewRepository.save(review);
        }
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

    @Transactional(readOnly = true)
    public List<ReviewCommentDTO> getCommentsByMemberId(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));

        return reviewCommentRepository.findByMemberOrderByCreatedAtDesc(member)
                .stream()
                .map(this::convertToCommentDTOForMember)
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

        // 실제로 삭제하지 않고 삭제 표시만 함
        comment.setContent("삭제된 댓글입니다.");
        comment.setDeleted(true);
        reviewCommentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public List<ReviewDTO> getReviewsByMemberId(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));

        return reviewRepository.findByMemberOrderByCreatedAtDesc(member)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReviewDTO> getRecentViewsByMemberId(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));

        return reviewViewRepository.findByMemberOrderByViewedAtDesc(member)
                .stream()
                .map(ReviewView::getReview)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
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
                .memberId(review.getMember().getMemberId())
                .memberDisplayName(review.getMember().getDisplayName())
                .recipeId(review.getRecipe() != null ? review.getRecipe().getRecipeId() : null)
                .recipeTitle(review.getRecipe() != null ? review.getRecipe().getRecipeTitle() : null)
                .createdAt(review.getCreatedAt())
                .viewCount(review.getViewCount())
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

    private ReviewCommentDTO convertToCommentDTOForMember(ReviewComment comment) {
        return ReviewCommentDTO.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .memberId(comment.getMember().getMemberId())
                .memberDisplayName(comment.getMember().getDisplayName())
                .reviewId(comment.getReview().getId())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .parentId(comment.getParent() != null ? comment.getParent().getId() : null)
                .isDeleted(comment.isDeleted())
                .build();
    }
}