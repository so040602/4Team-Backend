package com.recipe.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChatService {

    @Value("${openai.api.key}")
    private String openaiApiKey;

    @Value("${openai.api.url}")
    private String openaiApiUrl;

    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String getAIResponse(String message) {
        logger.info("Received message: {}", message);
        if (message.contains("고객센터")) {
            return "고객 센터로 안내해드리겠습니다. 카카오톡: haru2175@kakao.com 로 연락주시길 바랍니다.";
        } else if (message.contains("식당")) {
            return "저희는 식당이 아닙니다.! 저희 플랫폼을 이용해주셔서 감사합니다.";
            
        } else

        try {
            // OpenAI API 호출 예제
            String apiUrl = openaiApiUrl;
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(openaiApiKey);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "gpt-3.5-turbo");
            requestBody.put("messages", List.of(Map.of("role", "user", "content", message)));
            requestBody.put("max_tokens", 150);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, entity, String.class);
            logger.info("OpenAI API response: {}", response.getBody());

            // JSON 응답에서 content 필드 추출
            JsonNode root = objectMapper.readTree(response.getBody());
            String content = root.path("choices").get(0).path("message").path("content").asText();
            return content;
        } catch (Exception e) {
            logger.error("Error during OpenAI API call", e);
            return "고객 센터로 안내해드리겠습니다. 카카오톡: haru2175@kakao.com 로 연락주시길 바랍니다.";
        }
    }
}
