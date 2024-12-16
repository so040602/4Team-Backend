package com.recipe.Repository;

import com.recipe.entity.Member;
import com.recipe.entity.ReviewComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewCommentRepository extends JpaRepository<ReviewComment, Long> {
    // 회원이 작성한 댓글 목록 조회 (작성일 기준 내림차순)
    List<ReviewComment> findByMemberOrderByCreatedAtDesc(Member member);
    // Custom query methods can be added here
}
