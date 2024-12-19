package com.recipe.Repository;

import com.recipe.entity.CookingTool;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CookingToolRepository extends JpaRepository<CookingTool, Long> {
}
