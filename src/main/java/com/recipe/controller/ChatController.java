package com.recipe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.recipe.service.ChatService;

@RestController
@RequestMapping("/api")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping("/chat")
    public String handleMessage(@RequestBody String message) {
        // AI 서비스와의 통신 로직을 호출합니다.
        return chatService.getAIResponse(message);
    }
}
