package com.gpt.meetingnotes.common.advice;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.gpt.meetingnotes.common.dto.CommonResponse;
import com.gpt.meetingnotes.common.enums.ClientErrorCode;
import com.gpt.meetingnotes.common.enums.ResponseCode;
import com.gpt.meetingnotes.common.enums.ServerErrorCode;
import com.gpt.meetingnotes.common.enums.ServiceErrorCode;
import com.gpt.meetingnotes.common.enums.ValidationErrorCode;
import com.gpt.meetingnotes.common.exception.ApplicationException;
import com.gpt.meetingnotes.common.utils.ValidationMessageUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice(
		basePackages = {"com.gpt.meetingnotes.summary.controller.api"},
		annotations = {RestController.class})
@RequiredArgsConstructor
public class GlobalApiExceptionHandler {

    private final MessageSource messageSource;
    
    private ResponseEntity<CommonResponse<Object>> buildResponse(ResponseCode code, Locale locale, Object detailMessage) {
        String message = messageSource.getMessage(code.getMessageKey(), null, locale);
        return ResponseEntity
                .status(code.getHttpStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(new CommonResponse<>(code.getCode(), message, detailMessage));
    }
    
 // 1. 커스텀 예외 처리
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<CommonResponse<Object>> handleApplicationException(ApplicationException ex, Locale locale) {
        ResponseCode code = ex.getErrorCode();
        String detailMessage = ex.getCustomMessage() != null
                ? ex.getCustomMessage()
                : messageSource.getMessage(code.getMessageKey(), null, locale);

        log.error("[ApplicationException] code={}, message={}, exception={}", code.getCode(), detailMessage, ex.toString(), ex);
        return buildResponse(code, locale, null);
    }

    // 2. @Valid - RequestBody 검증 실패
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public ResponseEntity<CommonResponse<Object>> handleValidationException(Exception ex, Locale locale) {
        List<String> message = ValidationMessageUtils.getMessageList(ex, messageSource, locale);
        log.warn("[ValidationException] {}", message, ex);
        return buildResponse(ValidationErrorCode.INVALID_INPUT, locale, message);
    }
    
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<CommonResponse<Object>> handleMissingParameter(MissingServletRequestParameterException ex, Locale locale) {
        log.warn("[MissingParameter] {}", ex.getMessage(), ex);
        String detail = ex.getParameterName() + " parameter is missing";
        return buildResponse(ValidationErrorCode.MISSING_PARAMETER, locale, detail);
    }
    
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<CommonResponse<Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex, Locale locale) {
        log.warn("[TypeMismatch] {}", ex.getMessage(), ex);
        String detail = String.format("Parameter '%s' expects type '%s'",
                ex.getName(),
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown");

        return buildResponse(ValidationErrorCode.TYPE_MISMATCH, locale, detail);
    }

    // 4. 잘못된 요청 형식 (클라이언트 오류)
    @ExceptionHandler({
        IllegalArgumentException.class,
        HttpMessageNotReadableException.class
    })
    public ResponseEntity<CommonResponse<Object>> handleBadRequestExceptions(Exception ex, Locale locale) {
        log.warn("[BadRequest] {}", ex.toString(), ex);
        return buildResponse(ClientErrorCode.BAD_REQUEST, locale, ex.getMessage());
    }

    // 5. 인증/인가 실패
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<CommonResponse<Object>> handleAccessDeniedException(AccessDeniedException ex, Locale locale) {
        log.warn("[AccessDenied] {}", ex.toString(), ex);
        return buildResponse(ClientErrorCode.FORBIDDEN, locale, ex.getMessage());
    }

    // 6. 지원하지 않는 HTTP 메서드
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<CommonResponse<Object>> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex, Locale locale) {
        log.warn("[MethodNotAllowed] {}", ex.toString(), ex);
        return buildResponse(ClientErrorCode.METHOD_NOT_ALLOWED, locale, ex.getMessage());
    }

    // 7. IO 처리 오류
    @ExceptionHandler(IOException.class)
    public ResponseEntity<CommonResponse<Object>> handleIOException(IOException ex, Locale locale) {
        log.error("[IOException] {}", ex.toString(), ex);
        return buildResponse(ServiceErrorCode.FILE_IO_ERROR, locale, ex.getMessage());
    }

    // 8. NullPointerException
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<CommonResponse<Object>> handleNullPointerException(NullPointerException ex, Locale locale) {
        log.error("[NullPointerException] {}", ex.toString(), ex);
        return buildResponse(ServiceErrorCode.NULL_POINTER, locale, ex.getMessage());
    }

    // 9. 그 외 모든 미처리 예외
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse<Object>> handleUnhandledException(Exception ex, Locale locale) {
        log.error("[UnhandledException] {}", ex.toString(), ex);
        return buildResponse(ServerErrorCode.INTERNAL_ERROR, locale, ex.getMessage());
    }
}
