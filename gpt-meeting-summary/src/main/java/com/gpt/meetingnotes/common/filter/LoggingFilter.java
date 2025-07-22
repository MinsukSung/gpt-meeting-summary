package com.gpt.meetingnotes.common.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import com.gpt.meetingnotes.common.utils.LogUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LoggingFilter implements Filter {
	private static final int MAX_BODY_LENGTH = 5000;
    private static final Set<String> SENSITIVE_FIELDS = new HashSet<>(Arrays.asList("password", "token", "secret"));
    private static final List<String> EXCLUDE_PATH_PREFIXES = Arrays.asList("/swagger", "/v3/api-docs", "/favicon", "/health");
	
    @Override
	public void init(FilterConfig filterConfig) throws ServletException {
    	 log.info("[LoggingFilter] 초기화 완료");
	}
    
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            chain.doFilter(request, response);
            return;
        }
		
		 HttpServletRequest httpRequest = (HttpServletRequest) request;

	        // 제외 경로 필터링
	        String uri = httpRequest.getRequestURI();
	        if (shouldExclude(uri)) {
	            chain.doFilter(request, response);
	            return;
	        }

	        // 래퍼 처리
	        CachedBodyHttpServletRequest wrappedRequest = new CachedBodyHttpServletRequest(httpRequest);
	        CachedBodyHttpServletResponse wrappedResponse = new CachedBodyHttpServletResponse((HttpServletResponse) response);

	        // 요청 본문
	        String requestBody = StreamUtils.copyToString(wrappedRequest.getInputStream(), StandardCharsets.UTF_8);
	        String maskedRequestBody = LogUtils.maskSensitiveFields(requestBody, SENSITIVE_FIELDS);

	        log.info("[REQUEST] {} {}\nHeaders: {}\nBody: {}",
	                httpRequest.getMethod(),
	                uri,
	                LogUtils.extractHeaders(httpRequest),
	                LogUtils.abbreviate(maskedRequestBody, MAX_BODY_LENGTH));

	        try {
	            chain.doFilter(wrappedRequest, wrappedResponse);
	        } finally {
	            // 응답 본문
	            String responseBody = new String(wrappedResponse.getCacheBody(), StandardCharsets.UTF_8);
	            String maskedResponseBody = LogUtils.maskSensitiveFields(responseBody, SENSITIVE_FIELDS);

	            int status = wrappedResponse.getStatus();
	            if (status >= 400) {
	                log.warn("[RESPONSE] {} → Status: {}\nBody: {}",
	                        uri, status, LogUtils.abbreviate(maskedResponseBody, MAX_BODY_LENGTH));
	            } else {
	                log.info("[RESPONSE] {} → Status: {}\nBody: {}",
	                        uri, status, LogUtils.abbreviate(maskedResponseBody, MAX_BODY_LENGTH));
	            }

	            // 응답 복원
	            ServletOutputStream out = response.getOutputStream();
	            out.write(wrappedResponse.getCacheBody());
	            out.flush();
	        }
		
	}
	
	@Override
	public void destroy() {
		log.info("[LoggingFilter] 종료 및 자원 정리");
	}
	
	private boolean shouldExclude(String uri) {
        return EXCLUDE_PATH_PREFIXES.stream().anyMatch(uri::startsWith);
    }
    
}
