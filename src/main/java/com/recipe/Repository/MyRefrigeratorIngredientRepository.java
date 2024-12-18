package com.recipe.Repository;

import com.recipe.dto.MyRefrigeratorIngredientDTO;
import com.recipe.entity.Ingredient;
import com.recipe.entity.MyRefrigerator;
import com.recipe.entity.MyRefrigeratorIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MyRefrigeratorIngredientRepository extends JpaRepository<MyRefrigeratorIngredient, Long> {

    @Query("SELECT new com.recipe.dto.MyRefrigeratorIngredientDTO(" +
            "mri.refriIngreId, " +
            "mri.ingredient.id, " +
            "mri.myRefrigerator.refriId, " +
            "mri.regdate) " +
            "FROM MyRefrigeratorIngredient mri " +
            "WHERE mri.myRefrigerator.refriId = :refriId")
    List<MyRefrigeratorIngredientDTO> findByMyRefrigerator_RefriId(@Param("refriId") Long refriId);

    boolean existsByMyRefrigeratorAndIngredient(MyRefrigerator myRefrigerator, Ingredient ingredient);

    Optional<MyRefrigeratorIngredient> findByMyRefrigeratorRefriIdAndIngredientIngredientId(Long refriId, Long ingredientId);
}
