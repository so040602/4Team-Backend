package com.recipe.controller;

import com.recipe.entity.Menu;
import com.recipe.service.MenuService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 *
 */
@RestController
@RequestMapping("/menus")
@CrossOrigin(origins = "http://localhost:3000",
        allowedHeaders = "*",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE},
        allowCredentials = "true")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService){
        this.menuService = menuService;
    }
    @GetMapping("/season/{season}")
    public List<Menu> getMenusBySeason(@PathVariable String season) {
        return menuService.getMenusBySeason(season);
    }

    @GetMapping("/mealTime/{mealTime}")
    public List<Menu> getMenusByMealTime(@PathVariable String mealTime) {
        return menuService.getMenusByMealTime(mealTime);
    }

    @GetMapping("/random")
    public ResponseEntity<?> getRandomMenus() {
        try {
            List<Menu> menus = menuService.getRandomMenus();

            // 결과가 null이거나 비어있을 경우 빈 리스트 반환
            if (menus == null || menus.isEmpty()) {
                return ResponseEntity.ok().body(Collections.emptyList());
            }

            // Member 정보를 제외한 메뉴 정보만 반환하기 위한 DTO 변환
            List<Map<String, Object>> menuDTOs = menus.stream()
                    .map(menu -> {
                        Map<String, Object> dto = new HashMap<>();
                        dto.put("menuIdx", menu.getMenuIdx());
                        dto.put("name", menu.getName());
                        dto.put("description", menu.getDescription());
                        dto.put("image", menu.getImage());
                        dto.put("season", menu.getSeason());
                        dto.put("mealTime", menu.getMealTime());
                        dto.put("createdAt", menu.getCreatedAt());
                        dto.put("updatedAt", menu.getUpdatedAt());
                        return dto;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(menuDTOs);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body("서버 오류가 발생했습니다: " + e.getMessage());
        }
    }
}
