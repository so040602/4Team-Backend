package com.recipe.service;

import com.recipe.Repository.MenuRepository;
import com.recipe.entity.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;

    public List<Menu> getMenusBySeason(String season) {
        System.out.println(season);
        return menuRepository.findBySeason(season);
    }

    public List<Menu> getMenusByMealTime(String mealTime) {
        return menuRepository.findByMealTime(mealTime);
    }

    public List<Menu> getRandomMenus() {
        try {
            List<Menu> allMenus = menuRepository.findAll();

            if (allMenus.isEmpty()) {
                return Collections.emptyList();
            }

            Collections.shuffle(allMenus);
            int menuCount = Math.min(5, allMenus.size());
            return allMenus.subList(0, menuCount);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("메뉴를 가져오는 중 오류가 발생했습니다.", e);
        }
    }
}
