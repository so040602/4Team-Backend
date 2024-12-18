package com.recipe.service;

import com.recipe.Repository.*;
import com.recipe.dto.IngredientDTO;
import com.recipe.dto.MyRefrigeratorIngredientDTO;
import com.recipe.dto.RecipeDTO;
import com.recipe.entity.Ingredient;
import com.recipe.entity.Member;
import com.recipe.entity.MyRefrigerator;
import com.recipe.entity.MyRefrigeratorIngredient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MyRefrigeratorService {
    private final MyRefrigeratorRepository myRefrigeratorRepository;

    private final MemberRepository memberRepository;

    private final IngredientRepagitory ingredientRepagitory;

    private final MyRefrigeratorIngredientRepository myRefrigeratorIngredientRepository;

    private final RecipeIngredientRepository recipeIngredientRepository;

    public boolean MyRefrigeratorRepositoryIfExist(Long memberId){
        return myRefrigeratorRepository.existsByMemberMemberId(memberId);
    }

    public MyRefrigerator createMyRefrigerator(Long memberId){
        System.out.println("Creating refrigerator for memberId: " + memberId);
        //1. 멤버 존재 확인
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        //2. 이미 냉장고가 있는지 확인
        if(myRefrigeratorRepository.existsByMemberMemberId(memberId)){
            throw new RuntimeException("Refrigerator already exists for this member");
        }
        //3. 새 냉장고 생성
        MyRefrigerator myRefrigerator = MyRefrigerator.builder()
            .member(member)
            .build();
        return myRefrigeratorRepository.save(myRefrigerator);
    }

    public Long getRefriId(Long memberId){
        Long refriId = myRefrigeratorRepository.findByMember_MemberId(memberId);
        System.out.println(refriId);
        return myRefrigeratorRepository.findByMember_MemberId(memberId);
    }

    public List<MyRefrigeratorIngredientDTO> myRefriIngerd(Long refriId){
        List<MyRefrigeratorIngredientDTO> myRefrigeratorIngredientList = myRefrigeratorIngredientRepository.findByMyRefrigerator_RefriId(refriId);

        return myRefrigeratorIngredientList;
    }

    public List<IngredientDTO> ingredients(){
        List<IngredientDTO> ingredientList = ingredientRepagitory.findAllData();
        return ingredientList;
    }

    public List<MyRefrigeratorIngredientDTO> insertIngred(Long refriId, List<Long> ingredId){
        MyRefrigerator myRefrigerator = myRefrigeratorRepository.findById(refriId).orElseThrow(() -> new RuntimeException("Refrigerator not found"));

        Date currentDate = new Date();

        List<MyRefrigeratorIngredient> entities = new ArrayList<>();

        for(Long ingredientId : ingredId){

            Ingredient ingredient = ingredientRepagitory.findById(ingredientId).orElseThrow(() -> new RuntimeException("Ingredient not found"));

            boolean exists = myRefrigeratorIngredientRepository.existsByMyRefrigeratorAndIngredient(myRefrigerator, ingredient);

            if(!exists){
                MyRefrigeratorIngredient entity = new MyRefrigeratorIngredient();
                entity.setMyRefrigerator(myRefrigerator);
                entity.setIngredient(ingredient);
                entity.setRegdate(currentDate);

                entities.add(entity);
                System.out.println("entity is:" + entity);
            }
        }

        System.out.println("entity iss:" + entities);
        myRefrigeratorIngredientRepository.saveAll(entities);


        List<MyRefrigeratorIngredientDTO> myRefriIngreDto = myRefriIngerd(refriId);


        return myRefriIngreDto;
    }

    public List<MyRefrigeratorIngredientDTO> removeMyRefriIngred(Long refriId, Long ingredId) {
        Optional<MyRefrigeratorIngredient> myRefrigeratorIngredient = myRefrigeratorIngredientRepository.findByMyRefrigeratorRefriIdAndIngredientIngredientId(refriId, ingredId);

        if(myRefrigeratorIngredient.isPresent()){
            myRefrigeratorIngredientRepository.delete(myRefrigeratorIngredient.get());
        }

        return myRefriIngerd(refriId);
    }

    public List<RecipeDTO> recommendRecipe(List<Long> ingredientList){

        List<RecipeDTO> recipeDTOS = recipeIngredientRepository.findTopRecipesByIngredients(ingredientList);

        return recipeDTOS;
    }


}
