package com.gpt.meetingnotes.common.utils;

import java.util.Enumeration;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

public class LogUtils {
	 /**
     * 긴 문자열 본문을 지정된 길이로 자르고, 초과 시 '...'을 붙여 리턴한다.
     *
     * @param body 원본 문자열 (예: JSON 요청/응답 본문)
     * @param max 최대 허용 길이
     * @return 축약된 문자열
     */
    public static String abbreviate(String body, int max) {
        if (body == null) return "";
        return body.length() > max ? body.substring(0, max) + "..." : body;
    }

    /**
     * HttpServletRequest에서 모든 헤더를 추출하여 로그 출력용 문자열로 변환한다.
     *
     * @param request HttpServletRequest 객체
     * @return "key1: value1, key2: value2, ..." 형식의 문자열
     */
    public static String extractHeaders(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        StringBuilder sb = new StringBuilder("{ ");
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            sb.append(name).append(": ").append(request.getHeader(name)).append(", ");
        }
        return sb.append("}").toString();
    }

    /**
     * JSON 문자열 내에서 지정된 키워드(field명)에 해당하는 값을 마스킹 처리한다.
     * 주로 password, token, secret 등의 민감한 필드를 로그에서 감추기 위해 사용한다.
     *
     * 예:
     * input: {"username":"john","password":"abc123"}
     * output: {"username":"john","password":"****"}
     *
     * @param json 원본 JSON 문자열
     * @param keywords 마스킹할 키 필드명 목록
     * @return 마스킹된 JSON 문자열
     */
    public static String maskSensitiveFields(String json, Set<String> keywords) {
        if (json == null || json.isEmpty()) return "";
        String masked = json;
        for (String field : keywords) {
            String pattern = String.format("\"%s\"\\s*:\\s*\"(.*?)\"", field);
            masked = masked.replaceAll(pattern, String.format("\"%s\":\"****\"", field));
        }
        return masked;
    }
}
