package com.recipe.controller;

import com.recipe.dto.ReviewDTO;
import com.recipe.dto.ReviewCommentDTO;
import com.recipe.jwt.JWTUtil;
import com.recipe.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    private final Path uploadPath = Paths.get("uploads");
    private final JWTUtil jwtUtil; // JWTUtil 추가

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ReviewDTO> createReview(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("rating") Integer rating,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestHeader("Authorization") String token) {
        // "Bearer " 제거 후 토큰에서 memberId 추출
        String jwtToken = token.substring(7);
        Long memberId = jwtUtil.getMemberId(jwtToken);

        ReviewDTO reviewDTO = ReviewDTO.builder()
                .title(title)
                .content(content)
                .rating(rating)
                .memberId(memberId)
                .build();
        return ResponseEntity.ok(reviewService.createReview(reviewDTO, image));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDTO> getReviewById(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        // "Bearer " 제거 후 토큰에서 memberId 추출
        String jwtToken = token.substring(7);
        Long memberId = jwtUtil.getMemberId(jwtToken);
        return ResponseEntity.ok(reviewService.getReviewById(id, memberId));
    }

    @GetMapping
    public ResponseEntity<List<ReviewDTO>> getAllReviews() {
        return ResponseEntity.ok(reviewService.getAllReviews());
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ReviewDTO> updateReview(
            @PathVariable Long id,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("rating") Integer rating,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestHeader("Authorization") String token) {
        // "Bearer " 제거 후 토큰에서 memberId 추출
        String jwtToken = token.substring(7);
        Long memberId = jwtUtil.getMemberId(jwtToken);

        ReviewDTO reviewDTO = ReviewDTO.builder()
                .title(title)
                .content(content)
                .rating(rating)
                .memberId(memberId)
                .build();
        return ResponseEntity.ok(reviewService.updateReview(id, reviewDTO, image));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        Long memberId = jwtUtil.getMemberId(token.substring(7)); // "Bearer " 제거 후 토큰 파싱
        reviewService.deleteReview(id, memberId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/comments")
    public ResponseEntity<ReviewCommentDTO> addComment(@RequestBody ReviewCommentDTO commentDTO) {
        return ResponseEntity.ok(reviewService.addComment(commentDTO));
    }

    @PostMapping("/comments/reply")
    public ResponseEntity<ReviewCommentDTO> addReply(@RequestBody ReviewCommentDTO commentDTO) {
        return ResponseEntity.ok(reviewService.addReply(commentDTO));
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<ReviewCommentDTO>> getCommentsByReviewId(@PathVariable Long id) {
        List<ReviewCommentDTO> comments = reviewService.getCommentsByReviewId(id);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/images/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            Path file = uploadPath.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 내 리뷰 목록 조회
    @GetMapping("/my")
    public ResponseEntity<List<ReviewDTO>> getMyReviews(
            @RequestHeader("Authorization") String token) {
        String jwtToken = token.substring(7);
        Long memberId = jwtUtil.getMemberId(jwtToken);
        return ResponseEntity.ok(reviewService.getReviewsByMemberId(memberId));
    }

    // 최근 본 리뷰 목록 조회
    @GetMapping("/recent")
    public ResponseEntity<List<ReviewDTO>> getRecentViews(
            @RequestHeader("Authorization") String token) {
        String jwtToken = token.substring(7);
        Long memberId = jwtUtil.getMemberId(jwtToken);
        return ResponseEntity.ok(reviewService.getRecentViewsByMemberId(memberId));
    }

    // 댓글 수정
    @PutMapping("/comments/{id}")
    public ResponseEntity<ReviewCommentDTO> updateComment(
            @PathVariable Long id,
            @RequestBody ReviewCommentDTO commentDTO) {
        ReviewCommentDTO updatedComment = reviewService.updateComment(id, commentDTO);
        return ResponseEntity.ok(updatedComment);
    }

    // 댓글 삭제
    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        Long memberId = jwtUtil.getMemberId(token.substring(7)); // "Bearer " 제거 후 토큰 파싱
        reviewService.deleteComment(id, memberId);
        return ResponseEntity.noContent().build();
    }

    // 내 댓글 목록 조회
    @GetMapping("/comments/my")
    public ResponseEntity<List<ReviewCommentDTO>> getMyComments(
            @RequestHeader("Authorization") String token) {
        String jwtToken = token.substring(7);
        Long memberId = jwtUtil.getMemberId(jwtToken);
        return ResponseEntity.ok(reviewService.getCommentsByMemberId(memberId));
    }
}