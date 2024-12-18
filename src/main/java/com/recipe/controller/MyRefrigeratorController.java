package com.recipe.controller;

import com.recipe.dto.*;
import com.recipe.entity.MyRefrigerator;
import com.recipe.service.MyRefrigeratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/refri")
@RequiredArgsConstructor
public class MyRefrigeratorController {
    private final MyRefrigeratorService myRefrigeratorService;

    @GetMapping("/MyRefri")
    public boolean getMyRefigeratorExist(@RequestParam Long memberId){
        return  myRefrigeratorService.MyRefrigeratorRepositoryIfExist(memberId);
    }

    @PostMapping("/insertRefri")
    public ResponseEntity<MyRefrigerator> createRefrigerator(@RequestParam Long memberId){
        try{
            System.out.println("Received memberId: " + memberId);
            MyRefrigerator myRefrigerator = myRefrigeratorService.createMyRefrigerator(memberId);
            return ResponseEntity.ok(myRefrigerator);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/getrefriId")
    public Long getRefriIdByMemberId(@RequestBody MemberIdRequestDTO requestMemId){
        System.out.println("Received memberId: " + requestMemId.getMemberId());
        return myRefrigeratorService.getRefriId(requestMemId.getMemberId());
    }

    @GetMapping("/ingredients")
    public List<IngredientDTO> getIngredient(){
        System.out.println("Received Ingredient: ");
        return myRefrigeratorService.ingredients();
    }

    @GetMapping("/myingredients/{refriId}")
    public List<MyRefrigeratorIngredientDTO> getRefriIngred(@PathVariable Long refriId){
        System.out.println("Received refriId: " + refriId);
        System.out.println(myRefrigeratorService.myRefriIngerd(refriId));
        return  myRefrigeratorService.myRefriIngerd(refriId);
    }

    @PostMapping("/insertIngredient")
    public List<MyRefrigeratorIngredientDTO> insertIngred(@RequestBody RequestMyrefriIngredDTO requestMyrefriIngredDTO){
        System.out.println("Received refidId" + requestMyrefriIngredDTO.getRefriId());
        System.out.println("Received IngredientId" + requestMyrefriIngredDTO.getIngredId());

        return myRefrigeratorService.insertIngred(requestMyrefriIngredDTO.getRefriId(),requestMyrefriIngredDTO.getIngredId());
    }

    @PostMapping("/removeIngredient")
    public List<MyRefrigeratorIngredientDTO> removeMyRefriIngred(@RequestBody RequestRemoveMyRefriIngredDTO requestRemoveMyRefriIngredDTO) {
        System.out.println(requestRemoveMyRefriIngredDTO.getRefriId());
        System.out.println(requestRemoveMyRefriIngredDTO.getIngredientId());

        List<MyRefrigeratorIngredientDTO> removeMyrefriIngredient = myRefrigeratorService.removeMyRefriIngred(requestRemoveMyRefriIngredDTO.getRefriId(), requestRemoveMyRefriIngredDTO.getIngredientId());

        return removeMyrefriIngredient;
    }

    @PostMapping("/recommendRecipe")
    public List<RecipeDTO> recommendRecipe(@RequestBody List<Long> ingredientIdList){
        System.out.println(ingredientIdList);

        List<RecipeDTO> recipeDTOS = myRefrigeratorService.recommendRecipe(ingredientIdList);

        return recipeDTOS;
    }

}
