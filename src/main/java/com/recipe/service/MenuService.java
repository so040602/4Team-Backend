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
        List<Menu> allMenus = menuRepository.findAll();
        Collections.shuffle(allMenus);  // 메뉴를 섞어 랜덤화
        int menuCount = Math.min(5, allMenus.size());  // 5와 실제 메뉴 개수 중 작은 값 선택
        return allMenus.subList(0, menuCount);  // 가능한 만큼만 반환
    }
}
