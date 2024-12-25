package com.recipe.controller;

import com.recipe.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
public class ImageUploadController {

    private final ImageService imageService;

    @PostMapping("/image")
    public ResponseEntity<?> uploadImage(
            @RequestParam("file") MultipartFile file
    ) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("파일이 비어있습니다.");
        }

        // 파일 크기 체크 (예: 10MB)
        if (file.getSize() > 10 * 1024 * 1024) {
            return ResponseEntity.badRequest().body("파일 크기는 10MB를 초과할 수 없습니다.");
        }

        // 파일 형식 체크
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return ResponseEntity.badRequest().body("이미지 파일만 업로드 가능합니다.");
        }

        try {
            String filename = imageService.saveImage(file);
            return ResponseEntity.ok(filename);
        } catch (java.io.IOException e) {
            return ResponseEntity.badRequest().body("이미지 저장 중 오류 발생: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("예상치 못한 오류 발생: " + e.getMessage());
        }
    }
}
