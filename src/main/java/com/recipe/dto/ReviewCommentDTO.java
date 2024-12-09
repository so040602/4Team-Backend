package com.recipe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCommentDTO {
    private Long rev_comment_id;
    private String rev_comment_content;
    private String memberName;
    private Timestamp rev_comment_created_at;
    private Long parentCommentId;
    private List<ReviewCommentDTO> replies;
}