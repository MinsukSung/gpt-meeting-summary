package com.gpt.meetingnotes.common.advice;

import java.io.IOException;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.gpt.meetingnotes.common.dto.CommonResponse;
import com.gpt.meetingnotes.common.enums.ErrorCode;
import com.gpt.meetingnotes.common.exception.ApplicationException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice(
		basePackages = {"com.gpt.meetingnotes.summary.controller.api"},
		annotations = {RestController.class})
@RequiredArgsConstructor
public class GlobalApiExceptionHandler {

    private final MessageSource messageSource;

    // 1. 커스텀 예외 처리
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<CommonResponse<Object>> handleApplicationException(ApplicationException ex, Locale locale) {
        ErrorCode code = ex.getErrorCode();
        String message;

        if (ex.getCustomMessage() != null) {
            message = ex.getCustomMessage();
        } else {
            message = messageSource.getMessage(code.getMessageKey(), null, locale);
        }

        log.error("[ApplicationException] code={}, message={}, exception={}",
                  code.getCode(), message, ex.toString(), ex);

        return ResponseEntity
                .status(Integer.parseInt(code.getCode()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(new CommonResponse<>(code.getCode(), message, null));
    }

    // 2. @Valid 검증 실패 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse<Object>> handleValidationException(MethodArgumentNotValidException ex, Locale locale) {
        StringBuilder messageBuilder = new StringBuilder();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            messageBuilder.append("[")
                          .append(error.getField())
                          .append("] ")
                          .append(messageSource.getMessage(error.getDefaultMessage(), null, locale))
                          .append(" ");
        }

        String message = messageBuilder.toString().trim();

        log.warn("[ValidationException] {}", message, ex);

        return ResponseEntity
                .badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new CommonResponse<>(ErrorCode.INVALID_INPUT.getCode(), messageSource.getMessage(ErrorCode.INVALID_INPUT.getMessageKey(), null, locale), message));
    }
    
 // 2. @Valid 검증 실패 처리
    @ExceptionHandler(BindException.class)
    public ResponseEntity<CommonResponse<Object>> handleBindException(BindException ex, Locale locale) {
        StringBuilder messageBuilder = new StringBuilder();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            messageBuilder.append("[")
                          .append(error.getField())
                          .append("] ")
                          .append(messageSource.getMessage(error.getDefaultMessage(), null, locale))
                          .append(" ");
        }

        String message = messageBuilder.toString().trim();

        log.warn("[ValidationException] {}", message, ex);

        return ResponseEntity
                .badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new CommonResponse<>(ErrorCode.INVALID_INPUT.getCode(), messageSource.getMessage(ErrorCode.INVALID_INPUT.getMessageKey(), null, locale), message));
    }

    // 3. IOException, NullPointerException, 기타 예외 처리
    @ExceptionHandler({IOException.class, NullPointerException.class, Exception.class})
    public ResponseEntity<CommonResponse<Object>> handleGenericException(Exception ex, Locale locale) {
        ErrorCode code = mapToErrorCode(ex);
        String message = messageSource.getMessage(code.getMessageKey(), null, locale);

        log.error("[API-Exception] code={}, message={}, exception={}", code.getCode(), message, ex.toString(), ex);

        return ResponseEntity
                .status(Integer.parseInt(code.getCode()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(new CommonResponse<>(code.getCode(), message, null));
    }

    // 4. 예외 → ErrorCode 매핑
    private ErrorCode mapToErrorCode(Exception ex) {
        if (ex instanceof IOException) {
            return ErrorCode.FILE_IO_ERROR;
        }
        if (ex instanceof NullPointerException) {
            return ErrorCode.NULL_POINTER;
        }
        return ErrorCode.SERVER_ERROR;
    }
}
