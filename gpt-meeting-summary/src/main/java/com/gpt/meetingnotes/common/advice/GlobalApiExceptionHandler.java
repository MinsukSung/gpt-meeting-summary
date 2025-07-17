package com.gpt.meetingnotes.common.advice;

import java.io.IOException;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.gpt.meetingnotes.common.dto.CommonResponse;
import com.gpt.meetingnotes.common.enums.ErrorCode;
import com.gpt.meetingnotes.common.exception.ApplicationException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice(basePackages = "com.gpt.meetingnotes.summary.controller.api")
@RequiredArgsConstructor
public class GlobalApiExceptionHandler {
	
	private final MessageSource messageSource;
	
	// 커스텀 예외
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<CommonResponse<Object>> handleApplicationException(ApplicationException ex, Locale locale) {
    	
    	
        ErrorCode code = ex.getErrorCode();
        String message = ex.getCustomMessage() != null
        		? ex.getCustomMessage()
        		: messageSource.getMessage(code.getMessageKey(), null, locale);
        		
        log.error("[ApplicationException] code={}, message={}", code.getCode(), ex.getMessage(), ex);
            			
        return ResponseEntity
                .status(Integer.parseInt(code.getCode()))
                .body(new CommonResponse<>(code.getCode(), message, null));
    }

    @ExceptionHandler({IOException.class, NullPointerException.class, Exception.class})
    public ResponseEntity<CommonResponse<Object>> handleGenericException(Exception ex, Locale locale) {
        ErrorCode code = mapToErrorCode(ex);
        String message = messageSource.getMessage(code.getMessageKey(), null, locale);

        log.error("[API-Exception] code={}, message={}", code.getCode(), message, ex);

        return ResponseEntity
                .status(Integer.parseInt(code.getCode()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(new CommonResponse<>(code.getCode(), message, null));
    }

    private ErrorCode mapToErrorCode(Exception ex) {
        if (ex instanceof IOException) return ErrorCode.FILE_IO_ERROR;
        if (ex instanceof NullPointerException) return ErrorCode.NULL_POINTER;
        return ErrorCode.SERVER_ERROR;
    }
}
