package com.gpt.meetingnotes.common.advice;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.MethodParameter;
import org.springframework.core.io.InputStreamSource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.gpt.meetingnotes.common.dto.CommonResponse;
import com.gpt.meetingnotes.common.enums.ErrorCode;

import lombok.RequiredArgsConstructor;

@RestControllerAdvice(
		basePackages = {"com.gpt.meetingnotes.summary.controller.api"},
		annotations = {RestController.class})
@RequiredArgsConstructor
public class GlobalApiControllerAdvice implements ResponseBodyAdvice<Object> {
	
	private final MessageSource messageSource;

	//모든 Controller 응답감싸기
	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return true;
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
			ServerHttpResponse response) {
		
		if (body instanceof CommonResponse) return body;
		
		// 파일 응답 예외 처리
	    if (body instanceof Resource || body instanceof InputStreamSource || body instanceof byte[]) {
	        return body;
	    }
		
		return new CommonResponse<>(
				ErrorCode.SUCCESS.getCode(),
				messageSource.getMessage(ErrorCode.SUCCESS.getMessageKey(), null, LocaleContextHolder.getLocale()),
				body
			);
	}
	
	
}
