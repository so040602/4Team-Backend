package com.recipe.Repository;

import com.recipe.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findAllByOrderByRevCreatedAtDesc(Pageable pageable); // 수정된 부분

    @Query("SELECT r FROM Review r LEFT JOIN FETCH r.member WHERE r.rev_id = :reviewId")
    Optional<Review> findByIdWithMember(@Param("reviewId") Long reviewId);
}
