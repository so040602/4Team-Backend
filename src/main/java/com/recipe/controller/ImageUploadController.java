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
    public ResponseEntity<String> uploadImage(
            @RequestParam("file") MultipartFile file
    ) {
        try {
            String filename = imageService.saveImage(file);
            return ResponseEntity.ok(filename);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
