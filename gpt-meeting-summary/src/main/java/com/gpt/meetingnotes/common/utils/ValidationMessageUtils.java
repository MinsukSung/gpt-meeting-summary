package com.gpt.meetingnotes.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidationMessageUtils {
	 /**
     * @param ex MethodArgumentNotValidException or BindException
     * @param messageSource MessageSource (for i18n)
     * @param locale Locale
     * @return 단일 메시지 (줄 바꿈 없이 연결된 메시지들)
     */
    public static String getSingleMessage(Exception ex, MessageSource messageSource, Locale locale) {
        List<String> messages = getMessageList(ex, messageSource, locale);
        return String.join(" ", messages);
    }

    /**
     * @param ex MethodArgumentNotValidException or BindException
     * @param messageSource MessageSource
     * @param locale Locale
     * @return 필드별 메시지 리스트
     */
    public static List<String> getMessageList(Exception ex, MessageSource messageSource, Locale locale) {
        List<FieldError> fieldErrors = new ArrayList<>();

        if (ex instanceof MethodArgumentNotValidException) {
            fieldErrors = ((MethodArgumentNotValidException) ex).getBindingResult().getFieldErrors();
        } else if (ex instanceof BindException) {
            fieldErrors = ((BindException) ex).getBindingResult().getFieldErrors();
        }

        List<String> messages = new ArrayList<>();
        for (FieldError error : fieldErrors) {
           
            String code = error.getCode(); // e.g. "NotBlank", "Size" 등
            log.debug(">>>>>>>>>>>>>>>>> : {}", code);
            String resolvedMessage = messageSource.getMessage(error, locale);
            log.debug(">>>>>>>>>>>>>>>>> : {}", resolvedMessage);
            messages.add(resolvedMessage);
        }

        return messages;
    }
}
