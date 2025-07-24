package com.gpt.meetingnotes.config;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class MessageConverterConfig implements WebMvcConfigurer {
	
	private final ObjectMapper objectMapper;

	
	@Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 기본 제거
        converters.clear();

        //Jackson 기반 JSON 컨버터
        MappingJackson2HttpMessageConverter jacksonConverter = new MappingJackson2HttpMessageConverter(objectMapper) {
            @Override
            protected boolean supports(Class<?> clazz) {
                return true; // 모든 타입 처리, String 포함
            }
        };

        jacksonConverter.setDefaultCharset(StandardCharsets.UTF_8);
        converters.add(jacksonConverter);
    }
}
