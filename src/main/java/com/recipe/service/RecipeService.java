package com.recipe.service;

import com.recipe.dto.*;
import com.recipe.entity.*;
import com.recipe.Repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final MemberRepository memberRepository;
    private final IngredientRepository ingredientRepository;
    private final CookingToolRepository cookingToolRepository;
    private final RecipeStepRepository recipeStepRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final RecipeCookingToolRepository recipeCookingToolRepository;
    private final ImageService imageService;

    // 임시저장된 레시피 조회
    public RecipeCreateResponseDTO getTempSavedRecipe(Long memberId) {
        Recipe recipe = recipeRepository.findByMember_MemberIdAndRegistrationState(memberId, RegistrationState.TEMP)
                .orElse(null);

        if (recipe == null) {
            return null;
        }

        return toRecipeCreateResponseDTO(recipe);
    }

    // 임시저장 -> 저장 시 사용
    // 연관 엔티티들을 삭제하는 private 메서드 추가
    private void clearRecipeRelations(Recipe recipe) {
        if (recipe.getRecipeSteps() != null && !recipe.getRecipeSteps().isEmpty()) {
            recipeStepRepository.deleteAll(recipe.getRecipeSteps());
            recipe.getRecipeSteps().clear();
        }
        if (recipe.getRecipeIngredients() != null && !recipe.getRecipeIngredients().isEmpty()) {
            recipeIngredientRepository.deleteAll(recipe.getRecipeIngredients());
            recipe.getRecipeIngredients().clear();
        }
        if (recipe.getRecipeCookingTools() != null && !recipe.getRecipeCookingTools().isEmpty()) {
            recipeCookingToolRepository.deleteAll(recipe.getRecipeCookingTools());
            recipe.getRecipeCookingTools().clear();
        }
    }

    // 레시피 임시저장
    @Transactional
    public RecipeCreateResponseDTO tempSaveRecipe(RecipeTempSaveRequestDTO requestDTO) {
        Member member = memberRepository.findById(requestDTO.getMemberId())
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));

        // 기존 임시저장 레시피 조회
        Recipe existingRecipe = recipeRepository
                .findByMember_MemberIdAndRegistrationState(member.getMemberId(), RegistrationState.TEMP)
                .orElse(null);

        // 기존 이미지 URL 수집 및 삭제
        if (existingRecipe != null) {
            List<String> oldImages = new ArrayList<>();
            // 대표 이미지가 있고 새로운 대표 이미지와 다른 경우에만 삭제 목록에 추가
            if (existingRecipe.getRecipeThumbnail() != null &&
                    !existingRecipe.getRecipeThumbnail().equals(requestDTO.getRecipeThumbnail())) {
                oldImages.add(existingRecipe.getRecipeThumbnail());
            }

            // 기존 스텝 이미지들 중 새로운 스텝에 없는 이미지만 삭제 목록에 추가
            Map<Integer, String> newStepImages = new HashMap<>();
            if (requestDTO.getRecipeSteps() != null) {
                requestDTO.getRecipeSteps().forEach(step ->
                        newStepImages.put(step.getStepOrder(), step.getStepImage()));
            }

            existingRecipe.getRecipeSteps().forEach(step -> {
                if (step.getStepImage() != null &&
                        !newStepImages.containsValue(step.getStepImage())) {
                    oldImages.add(step.getStepImage());
                }
            });

            // 수집된 이미지 삭제
            if (!oldImages.isEmpty()) {
                imageService.deleteAllImages(oldImages);
            }

            // 기존 데이터 삭제
            clearRecipeRelations(existingRecipe);
            recipeRepository.delete(existingRecipe);
        }

        // 새로운 Recipe 객체 생성
        Recipe newRecipe = Recipe.builder()
                .member(member)
                .recipeTitle(requestDTO.getRecipeTitle())
                .recipeThumbnail(requestDTO.getRecipeThumbnail())
                .servingSize(requestDTO.getServingSize())
                .cookingTime(requestDTO.getCookingTime())
                .difficultyLevel(requestDTO.getDifficultyLevel())
                .situation(requestDTO.getSituation())
                .recipeTip(requestDTO.getRecipeTip())
                .registrationState(RegistrationState.TEMP)
                .recipeSteps(new ArrayList<>())
                .recipeIngredients(new ArrayList<>())
                .recipeCookingTools(new ArrayList<>())
                .build();

        // 레시피 스텝 설정
        if (requestDTO.getRecipeSteps() != null) {
            List<RecipeStep> steps = requestDTO.getRecipeSteps().stream()
                    .map(stepDTO -> RecipeStep.builder()
                            .recipe(newRecipe)
                            .stepOrder(stepDTO.getStepOrder())
                            .stepDescription(stepDTO.getStepDescription())
                            .stepImage(stepDTO.getStepImage())
                            .build())
                    .collect(Collectors.toList());
            newRecipe.getRecipeSteps().addAll(steps);
        }

        // 레시피 재료 설정
        if (requestDTO.getRecipeIngredients() != null) {
            List<RecipeIngredient> ingredients = requestDTO.getRecipeIngredients().stream()
                    .map(ingredientDTO -> {
                        Ingredient ingredient = ingredientRepository.findById(ingredientDTO.getIngredientId())
                                .orElseThrow(() -> new EntityNotFoundException("Ingredient not found"));
                        return RecipeIngredient.builder()
                                .recipe(newRecipe)
                                .ingredient(ingredient)
                                .ingredientAmount(ingredientDTO.getIngredientAmount())
                                .build();
                    })
                    .collect(Collectors.toList());
            newRecipe.getRecipeIngredients().addAll(ingredients);
        }

        // 레시피 조리도구 설정
        if (requestDTO.getRecipeCookingTools() != null) {
            List<RecipeCookingTool> cookingTools = requestDTO.getRecipeCookingTools().stream()
                    .map(toolDTO -> {
                        CookingTool cookingTool = cookingToolRepository.findById(toolDTO.getCookingToolId())
                                .orElseThrow(() -> new EntityNotFoundException("CookingTool not found"));
                        return RecipeCookingTool.builder()
                                .recipe(newRecipe)
                                .cookingTool(cookingTool)
                                .build();
                    })
                    .collect(Collectors.toList());
            newRecipe.getRecipeCookingTools().addAll(cookingTools);
        }

        Recipe savedRecipe = recipeRepository.save(newRecipe);
        return toRecipeCreateResponseDTO(savedRecipe);
    }

    // 레시피 최종 저장
    @Transactional
    public RecipeCreateResponseDTO createRecipe(RecipeCreateRequestDTO requestDTO) {
        Member member = memberRepository.findById(requestDTO.getMemberId())
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));

        // 기존 임시저장 레시피 조회
        Recipe existingRecipe = recipeRepository
                .findByMember_MemberIdAndRegistrationState(member.getMemberId(), RegistrationState.TEMP)
                .orElse(null);

        // 기존 이미지 URL 수집 및 삭제
        if (existingRecipe != null) {
            List<String> oldImages = new ArrayList<>();
            // 대표 이미지가 있고 새로운 대표 이미지와 다른 경우에만 삭제 목록에 추가
            if (existingRecipe.getRecipeThumbnail() != null &&
                    !existingRecipe.getRecipeThumbnail().equals(requestDTO.getRecipeThumbnail())) {
                oldImages.add(existingRecipe.getRecipeThumbnail());
            }

            // 기존 스텝 이미지들 중 새로운 스텝에 없는 이미지만 삭제 목록에 추가
            Map<Integer, String> newStepImages = new HashMap<>();
            if (requestDTO.getRecipeSteps() != null) {
                requestDTO.getRecipeSteps().forEach(step ->
                        newStepImages.put(step.getStepOrder(), step.getStepImage()));
            }

            existingRecipe.getRecipeSteps().forEach(step -> {
                if (step.getStepImage() != null &&
                        !newStepImages.containsValue(step.getStepImage())) {
                    oldImages.add(step.getStepImage());
                }
            });

            // 수집된 이미지 삭제
            if (!oldImages.isEmpty()) {
                imageService.deleteAllImages(oldImages);
            }

            // 기존 데이터 삭제
            clearRecipeRelations(existingRecipe);
            recipeRepository.delete(existingRecipe);
        }

        // 새로운 Recipe 객체 생성
        Recipe newRecipe = Recipe.builder()
                .member(member)
                .recipeTitle(requestDTO.getRecipeTitle())
                .recipeThumbnail(requestDTO.getRecipeThumbnail())
                .servingSize(requestDTO.getServingSize())
                .cookingTime(requestDTO.getCookingTime())
                .difficultyLevel(requestDTO.getDifficultyLevel())
                .situation(requestDTO.getSituation())
                .recipeTip(requestDTO.getRecipeTip())
                .registrationState(RegistrationState.PUBLISHED)
                .recipeSteps(new ArrayList<>())
                .recipeIngredients(new ArrayList<>())
                .recipeCookingTools(new ArrayList<>())
                .build();

        // 레시피 스텝 설정
        if (requestDTO.getRecipeSteps() != null) {
            List<RecipeStep> steps = requestDTO.getRecipeSteps().stream()
                    .map(stepDTO -> RecipeStep.builder()
                            .recipe(newRecipe)
                            .stepOrder(stepDTO.getStepOrder())
                            .stepDescription(stepDTO.getStepDescription())
                            .stepImage(stepDTO.getStepImage())
                            .build())
                    .collect(Collectors.toList());
            newRecipe.getRecipeSteps().addAll(steps);
        }

        // 레시피 재료 설정
        if (requestDTO.getRecipeIngredients() != null) {
            List<RecipeIngredient> ingredients = requestDTO.getRecipeIngredients().stream()
                    .map(ingredientDTO -> {
                        Ingredient ingredient = ingredientRepository.findById(ingredientDTO.getIngredientId())
                                .orElseThrow(() -> new EntityNotFoundException("Ingredient not found"));
                        return RecipeIngredient.builder()
                                .recipe(newRecipe)
                                .ingredient(ingredient)
                                .ingredientAmount(ingredientDTO.getIngredientAmount())
                                .build();
                    })
                    .collect(Collectors.toList());
            newRecipe.getRecipeIngredients().addAll(ingredients);
        }

        // 레시피 조리도구 설정
        if (requestDTO.getRecipeCookingTools() != null) {
            List<RecipeCookingTool> cookingTools = requestDTO.getRecipeCookingTools().stream()
                    .map(toolDTO -> {
                        CookingTool cookingTool = cookingToolRepository.findById(toolDTO.getCookingToolId())
                                .orElseThrow(() -> new EntityNotFoundException("CookingTool not found"));
                        return RecipeCookingTool.builder()
                                .recipe(newRecipe)
                                .cookingTool(cookingTool)
                                .build();
                    })
                    .collect(Collectors.toList());
            newRecipe.getRecipeCookingTools().addAll(cookingTools);
        }

        Recipe savedRecipe = recipeRepository.save(newRecipe);
        return toRecipeCreateResponseDTO(savedRecipe);
    }

    // 레시피 목록 가져오기
    public List<RecipeDTO> getAllRecipes() {
        List<Recipe> recipes = recipeRepository.findByRegistrationState(RegistrationState.PUBLISHED);
        return recipes.stream()
                .map(recipe -> RecipeDTO.builder()
                        .recipeId(recipe.getRecipeId())
                        .recipeTitle(recipe.getRecipeTitle())
                        .recipeThumbnail(recipe.getRecipeThumbnail())
                        .build())
                .collect(Collectors.toList());
    }

    public List<RecipeDTO> getRecipesByMemberId(Long memberId) {
        List<Recipe> recipes = recipeRepository.findAllByMember_MemberIdAndRegistrationState(memberId, RegistrationState.PUBLISHED);
        return recipes.stream()
                .map(recipe -> RecipeDTO.builder()
                        .recipeId(recipe.getRecipeId())
                        .memberId(recipe.getMember().getMemberId())
                        .recipeTitle(recipe.getRecipeTitle())
                        .recipeThumbnail(recipe.getRecipeThumbnail())
                        .recipeTip(recipe.getRecipeTip())
                        .build())
                .collect(Collectors.toList());
    }

    public Long getRecipeCount(Long memberId) {
        return recipeRepository.countByMember_MemberIdAndRegistrationState(memberId, RegistrationState.PUBLISHED);
    }

    // 응답 DTO 생성 메서드
    private RecipeCreateResponseDTO toRecipeCreateResponseDTO(Recipe recipe) {
        return RecipeCreateResponseDTO.builder()
                .recipeId(recipe.getRecipeId())
                .memberId(recipe.getMember().getMemberId())
                .recipeTitle(recipe.getRecipeTitle())
                .recipeThumbnail(recipe.getRecipeThumbnail())
                .servingSize(recipe.getServingSize())
                .cookingTime(recipe.getCookingTime())
                .difficultyLevel(recipe.getDifficultyLevel())
                .situation(recipe.getSituation())
                .recipeTip(recipe.getRecipeTip())
                .recipeSteps(recipe.getRecipeSteps().stream()
                        .map(step -> RecipeStepDTO.builder()
                                .stepOrder(step.getStepOrder())
                                .stepDescription(step.getStepDescription())
                                .stepImage(step.getStepImage())
                                .build())
                        .collect(Collectors.toList()))
                .recipeIngredients(recipe.getRecipeIngredients().stream()
                        .map(ingredient -> RecipeIngredientDTO.builder()
                                .ingredientId(ingredient.getIngredient().getIngredientId())
                                .ingredientName(ingredient.getIngredient().getIngredientName())
                                .ingredientAmount(ingredient.getIngredientAmount())
                                .build())
                        .collect(Collectors.toList()))
                .recipeCookingTools(recipe.getRecipeCookingTools().stream()
                        .map(tool -> RecipeCookingToolDTO.builder()
                                .cookingToolId(tool.getCookingTool().getCookingToolId())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}