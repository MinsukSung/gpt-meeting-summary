package com.gpt.meetingnotes.summary.controller.api;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gpt.meetingnotes.common.dto.CommonResponse;
import com.gpt.meetingnotes.common.enums.ErrorCode;
import com.gpt.meetingnotes.common.exception.ApplicationException;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestApiController {

    private final MessageSource messageSource;

    /**
     * 예외 테스트용 - 커스텀 예외 발생
     */
    @GetMapping("/exception")
    public String throwApplicationException() {
        throw new ApplicationException(ErrorCode.SUMMARY_FAILED);
    }

    /**
     * 예외 테스트용 - NPE 강제 발생
     */
    @GetMapping("/null")
    public String throwNullPointer() {
        String test = null;
        return test.toString();  // NPE 발생
    }

    /**
     * 다국어 메시지 테스트
     */
    @GetMapping("/i18n")
    public CommonResponse<String> testI18n(Locale locale) {
        String message = messageSource.getMessage("response.success", null, locale);
        return new CommonResponse<>("200", message, "테스트 메시지 출력");
    }
}