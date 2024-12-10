package com.recipe.controller;

import com.recipe.dto.ReviewDTO;
import com.recipe.dto.ReviewCommentDTO;
import com.recipe.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping
    public ResponseEntity<Page<ReviewDTO>> getReviewList(
            @PageableDefault(size = 10, sort = "rev_created_at", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(reviewService.getReviewList(pageable));
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewDTO> getReviewDetail(@PathVariable Long reviewId) {
        return ResponseEntity.ok(reviewService.getReviewDetail(reviewId));
    }

    @PostMapping
    public ResponseEntity<ReviewDTO> createReview(@RequestBody ReviewDTO reviewDTO) {
        return ResponseEntity.ok(reviewService.createReview(reviewDTO));
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewDTO> updateReview(
            @PathVariable Long reviewId,
            @RequestBody ReviewDTO reviewDTO) {
        return ResponseEntity.ok(reviewService.updateReview(reviewId, reviewDTO));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{reviewId}/like")
    public ResponseEntity<Void> toggleLike(@PathVariable Long reviewId) {
        reviewService.toggleLike(reviewId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{reviewId}/comments")
    public ResponseEntity<ReviewCommentDTO> addComment(
            @PathVariable Long reviewId,
            @RequestBody ReviewCommentDTO commentDTO) {
        return ResponseEntity.ok(reviewService.addComment(reviewId, commentDTO));
    }
}