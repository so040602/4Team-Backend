package com.recipe.controller;

import com.recipe.entity.Menu;
import com.recipe.service.MenuService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 
 */
@RestController
@RequestMapping("/menus")
@CrossOrigin(origins = "*")
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
    public List<Menu> getRandomMenus() {
        return menuService.getRandomMenus();
    }
}
