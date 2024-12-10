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
public class ReviewDTO {
    private Long rev_id;
    private String rev_title;
    private String rev_content;
    private String rev_image_url;
    private Integer rev_view_count;
    private Timestamp rev_created_at;
    private Timestamp rev_updated_at;
    private String memberName;
    private boolean isLiked;
    private int likeCount;
    private List<ReviewCommentDTO> comments;
}