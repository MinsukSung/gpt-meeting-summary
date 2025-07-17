package com.gpt.meetingnotes.common.utils;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.MediaType;

public class RequestUtils {
	 // ---------------------------
    // API 요청 판별 유틸
    // ---------------------------
    private boolean isApiRequest(HttpServletRequest request) {
        String accept = request.getHeader("Accept");
        String uri = request.getRequestURI();
        
        return (accept != null && accept.contains(MediaType.APPLICATION_JSON_VALUE)) || (uri != null && uri.startsWith("/api/"));
    }
    
}
