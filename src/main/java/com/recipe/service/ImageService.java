package com.recipe.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService {

    private static final Logger log = LoggerFactory.getLogger(ImageService.class);

    @Value("${app.image.directory}")
    private String imageDirectory;

    public String saveImage(MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(imageDirectory);

        // 타임스탬프와 원본 파일명으로 새 파일명 생성
        String originalFilename = file.getOriginalFilename();
        String timestamp = String.valueOf(System.currentTimeMillis());
        String filename = timestamp + "_" + originalFilename;

        Path filePath = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return filename;
    }

    public void deleteAllImages(List<String> imageUrls) {
        if (imageUrls == null || imageUrls.isEmpty()) {
            return;
        }

        for (String imageUrl : imageUrls) {
            try {
                // URL이 null이거나 빈 문자열인 경우 스킵
                if (imageUrl == null || imageUrl.trim().isEmpty()) {
                    continue;
                }

                // /images/1234567890_example.jpg 형태의 전체 URL에서
                // 1234567890_example.jpg 형태의 전체 파일명 추출
                String fullFilename = imageUrl.substring(imageUrl.lastIndexOf('/') + 1);
                Path imagePath = Paths.get(imageDirectory, fullFilename);

                if (Files.exists(imagePath)) {
                    Files.delete(imagePath);
                    log.info("이미지 파일 삭제 성공: {}", imagePath);
                } else {
                    log.warn("삭제할 이미지 파일이 존재하지 않음: {}", imagePath);
                }
            } catch (Exception e) {
                log.error("이미지 삭제 중 오류 발생: {}, 에러: {}", imageUrl, e.getMessage());
            }
        }
    }
}
