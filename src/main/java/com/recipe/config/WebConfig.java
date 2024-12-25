package com.recipe.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.image.directory}")
    private String imageDirectory;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/favicon.ico")
                .addResourceLocations("classpath:/static/");

        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");

        // 이미지 파일 접근을 위한 리소스 핸들러
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + imageDirectory + "/")
                .setCachePeriod(3600);
    }
}
