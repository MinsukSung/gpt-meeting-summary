package com.gpt.meetingnotes.common.advice;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.gpt.meetingnotes.common.dto.CommonResponse;
import com.gpt.meetingnotes.common.enums.ErrorCode;

import lombok.RequiredArgsConstructor;

@RestControllerAdvice(basePackages = "com.gpt.meetingnotes.summary.controller.api")
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
		
		return new CommonResponse<>(
				ErrorCode.SUCCESS.getCode(),
				messageSource.getMessage(ErrorCode.SUCCESS.getMessageKey(), null, Locale.getDefault()),
				body
			);
	}
	
	
}
