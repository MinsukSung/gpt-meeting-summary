package com.gpt.meetingnotes.common.advice;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.gpt.meetingnotes.common.enums.ResponseCode;
import com.gpt.meetingnotes.common.enums.ServerErrorCode;
import com.gpt.meetingnotes.common.enums.ServiceErrorCode;
import com.gpt.meetingnotes.common.enums.ValidationErrorCode;
import com.gpt.meetingnotes.common.exception.ApplicationException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice(basePackages = {"com.gpt.meetingnotes.summary.controller.web"}, 
annotations = {Controller.class})
@RequiredArgsConstructor
public class GlobalWebExceptionHandler {

    private final MessageSource messageSource;

    // 1. ApplicationException
    @ExceptionHandler(ApplicationException.class)
    public ModelAndView handleApplicationException(ApplicationException ex, HttpServletRequest request, Locale locale) {
        ResponseCode code = ex.getErrorCode();
        String message = ex.getCustomMessage() != null
                ? ex.getCustomMessage()
                : messageSource.getMessage(code.getMessageKey(), null, locale);

        log.error("[Web-ApplicationException] code={}, message={}, exception={}", code.getCode(), message, ex.toString(), ex);

        setErrorAttributes(request, code, message, null);
        return new ModelAndView("error/serviceError");
    }

    // 2. @Valid - RequestBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ModelAndView handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request, Locale locale) {
        String detailMessage = buildFieldErrorMessage(ex, locale);
        String summaryMessage = messageSource.getMessage(ValidationErrorCode.INVALID_INPUT.getMessageKey(), null, locale);

        log.warn("[Web-ValidationException] {}", detailMessage, ex);

        setErrorAttributes(request, ValidationErrorCode.INVALID_INPUT, summaryMessage, detailMessage);
        return new ModelAndView("error/validationError");
    }

    // 3. @Valid - Form/Query
    @ExceptionHandler(BindException.class)
    public ModelAndView handleBindException(BindException ex, HttpServletRequest request, Locale locale) {
        String detailMessage = buildFieldErrorMessage(ex, locale);
        String summaryMessage = messageSource.getMessage(ValidationErrorCode.INVALID_INPUT.getMessageKey(), null, locale);

        log.warn("[Web-BindValidationException] {}", detailMessage, ex);

        setErrorAttributes(request, ValidationErrorCode.INVALID_INPUT, summaryMessage, detailMessage);
        return new ModelAndView("error/validationError");
    }

    // 4. Generic / 서버 에러
    @ExceptionHandler({IOException.class, NullPointerException.class, Exception.class})
    public ModelAndView handleGenericException(Exception ex, HttpServletRequest request, Locale locale) {
        ResponseCode code = mapToErrorCode(ex);
        String message = messageSource.getMessage(code.getMessageKey(), null, locale);

        log.error("[Web-Exception] code={}, message={}, exception={}", code.getCode(), message, ex.toString(), ex);

        setErrorAttributes(request, code, message, null);
        return new ModelAndView("error/serviceError");
    }

    //예외 종류 → ResponseCode 매핑
    private ResponseCode mapToErrorCode(Exception ex) {
        if (ex instanceof IOException) return ServiceErrorCode.FILE_IO_ERROR;
        if (ex instanceof NullPointerException) return ServiceErrorCode.NULL_POINTER;
        return ServerErrorCode.INTERNAL_ERROR;
    }

    //필드 에러 메시지 조합 (공통)
    private String buildFieldErrorMessage(BindException ex, Locale locale) {
        StringBuilder builder = new StringBuilder();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
        	String fieldName = error.getField();
        	String message = messageSource.getMessage(error.getCode(), new Object[]{fieldName}, locale);
        	builder.append(message).append(" ");
        }
        return builder.toString().trim();
    }

    //request 속성 설정
    private void setErrorAttributes(HttpServletRequest request, ResponseCode code, String message, String fieldErrors) {
        request.setAttribute("errorCode", code.getCode());
        request.setAttribute("errorMessage", message);
        if (fieldErrors != null) {
            request.setAttribute("fieldErrors", fieldErrors);
        }
    }
}