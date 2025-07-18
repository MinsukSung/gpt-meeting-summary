package com.gpt.meetingnotes.utils;

import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestUtils {
	private static final ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    public static void printPrettyJson(MvcResult result) {
        try {
        	String content = result.getResponse().getContentAsString();
        	if (!content.trim().startsWith("{") && !content.trim().startsWith("[")) {
                log.info("응답 (non-JSON): {}", content);
                return;
            }
            JsonNode jsonNode = objectMapper.readTree(content);
            String pretty = objectMapper.writeValueAsString(jsonNode);
            log.info("응답 JSON (pretty):\n{}", pretty);
        } catch (Exception e) {
        	log.error("JSON 출력 실패: " + e.getMessage());
        }
    }
}
