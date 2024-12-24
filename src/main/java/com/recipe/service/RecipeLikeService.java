package com.recipe.service;

import com.recipe.Repository.MemberRepository;
import com.recipe.Repository.RecipeLikeRepository;
import com.recipe.Repository.RecipeRepository;
import com.recipe.dto.RecipeLikeResponseDTO;
import com.recipe.entity.Member;
import com.recipe.entity.Recipe;
import com.recipe.entity.RecipeLike;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RecipeLikeService {
    private final RecipeLikeRepository recipeLikeRepository;
    private final RecipeRepository recipeRepository;
    private final MemberRepository memberRepository;

    public RecipeLikeResponseDTO toggleLike(Long recipeId, Long memberId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new IllegalArgumentException("Recipe not found"));

        boolean exists = recipeLikeRepository.existsByRecipe_RecipeIdAndMember_MemberId(recipeId, memberId);

        if (exists) {
            // 좋아요 취소
            recipeLikeRepository.deleteByRecipe_RecipeIdAndMember_MemberId(recipeId, memberId);
            recipe.setLikeCount(recipe.getLikeCount() - 1);
        } else {
            // 좋아요 추가
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new IllegalArgumentException("Member not found"));

            RecipeLike recipeLike = RecipeLike.builder()
                    .recipe(recipe)
                    .member(member)
                    .build();

            recipeLikeRepository.save(recipeLike);
            recipe.setLikeCount(recipe.getLikeCount() + 1);
        }

        recipeRepository.save(recipe);

        return RecipeLikeResponseDTO.builder()
                .liked(!exists)
                .likeCount(recipe.getLikeCount())
                .build();
    }
}
