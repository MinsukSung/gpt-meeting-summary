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

import com.gpt.meetingnotes.common.enums.ErrorCode;
import com.gpt.meetingnotes.common.exception.ApplicationException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice(basePackages = {"com.gpt.meetingnotes.summary.controller.web"}, 
annotations = {Controller.class})
@RequiredArgsConstructor
public class GlobalWebExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(ApplicationException.class)
    public ModelAndView handleApplicationException(ApplicationException ex, HttpServletRequest request, Locale locale) {
        ErrorCode code = ex.getErrorCode();
        String message = ex.getCustomMessage() != null
                ? ex.getCustomMessage()
                : messageSource.getMessage(code.getMessageKey(), null, locale);

        log.error("[Web-ApplicationException] code={}, message={}, exception={}",
                        code.getCode(), message, ex.toString(), ex);

        request.setAttribute("errorCode", code.name());
        request.setAttribute("errorMessage", message);
        return new ModelAndView("error/serviceError");
    }
    
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ModelAndView handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request, Locale locale) {
        StringBuilder messageBuilder = new StringBuilder();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            messageBuilder.append("[")
                          .append(error.getField())
                          .append("] ")
                          .append(messageSource.getMessage(error.getDefaultMessage(), null, locale))
                          .append(" ");
        }

        String fieldErrors = messageBuilder.toString().trim();
        
        request.setAttribute("errorCode", ErrorCode.INVALID_INPUT.name());
        request.setAttribute("errorMessage", messageSource.getMessage(ErrorCode.INVALID_INPUT.getMessageKey(), null, locale));
        request.setAttribute("fieldErrors", fieldErrors);
        
        log.warn("[Web-ValidationException] {}", fieldErrors, ex);

        return new ModelAndView("error/validationError");
    }
    
    @ExceptionHandler(BindException.class)
    public ModelAndView handleBindException(BindException ex, HttpServletRequest request, Locale locale) {
        StringBuilder messageBuilder = new StringBuilder();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            messageBuilder.append("[")
                          .append(error.getField())
                          .append("] ")
                          .append(messageSource.getMessage(error.getDefaultMessage(), null, locale))
                          .append(" ");
        }

        String fieldErrors = messageBuilder.toString().trim();
        
        request.setAttribute("errorCode", ErrorCode.INVALID_INPUT.name());
        request.setAttribute("errorMessage", messageSource.getMessage(ErrorCode.INVALID_INPUT.getMessageKey(), null, locale));
        request.setAttribute("fieldErrors", fieldErrors);
        
        log.warn("[Web-ValidationException] {}", fieldErrors, ex);

        return new ModelAndView("error/validationError");
    }
    
    @ExceptionHandler({IOException.class, NullPointerException.class, Exception.class})
    public ModelAndView handleGenericException(Exception ex, HttpServletRequest request, Locale locale) {
        ErrorCode code = mapToErrorCode(ex);
        String message = messageSource.getMessage(code.getMessageKey(), null, locale);

        log.error("[Web-Exception] code={}, message={}, exception={}", code.getCode(), message, ex.toString(), ex);

        request.setAttribute("errorCode", code.name());
        request.setAttribute("errorMessage", message);
        return new ModelAndView("error/serviceError");
    }

    private ErrorCode mapToErrorCode(Exception ex) {
        if (ex instanceof IOException) return ErrorCode.FILE_IO_ERROR;
        if (ex instanceof NullPointerException) return ErrorCode.NULL_POINTER;
        return ErrorCode.SERVER_ERROR;
    }
}