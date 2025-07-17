package com.gpt.meetingnotes.common.advice;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.gpt.meetingnotes.common.enums.ErrorCode;
import com.gpt.meetingnotes.common.exception.ApplicationException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice(basePackages = "com.gpt.meetingnotes.summary.controller.web")
@RequiredArgsConstructor
public class GlobalWebExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(ApplicationException.class)
    public ModelAndView handleApplicationException(ApplicationException ex, HttpServletRequest request, Locale locale) {
        ErrorCode code = ex.getErrorCode();
        String message = ex.getCustomMessage() != null
                ? ex.getCustomMessage()
                : messageSource.getMessage(code.getMessageKey(), null, locale);

        log.error("[WEB-ApplicationException] code={}, message={}", code.getCode(), message, ex);

        request.setAttribute("errorCode", code.name());
        request.setAttribute("errorMessage", message);
        return new ModelAndView("error/serviceError");
    }

    @ExceptionHandler({IOException.class, NullPointerException.class, Exception.class})
    public ModelAndView handleGenericException(Exception ex, HttpServletRequest request, Locale locale) {
        ErrorCode code = mapToErrorCode(ex);
        String message = messageSource.getMessage(code.getMessageKey(), null, locale);

        log.error("[WEB-Exception] code={}, message={}", code.getCode(), message, ex);

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