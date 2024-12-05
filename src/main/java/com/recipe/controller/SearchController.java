package com.recipe.controller;


import com.recipe.dto.NaverSearchResponse;
import com.recipe.service.NaverSearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@CrossOrigin(origins = "http://localhost:3000") // React 앱의 URL(http://localhost:3000)에서 오는 요청을 허용
public class SearchController {

    private final NaverSearchService naverSearchService;


    public SearchController(NaverSearchService naverSearchService) {
        this.naverSearchService = naverSearchService;
    }


    @GetMapping("/api/search")
    public NaverSearchResponse search(@RequestParam String query) {
        return naverSearchService.search(query);
    }
}
