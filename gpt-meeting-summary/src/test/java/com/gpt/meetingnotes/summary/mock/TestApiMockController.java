package com.gpt.meetingnotes.summary.mock;

import java.util.Locale;

import javax.validation.Valid;

import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gpt.meetingnotes.common.enums.ServiceErrorCode;
import com.gpt.meetingnotes.common.exception.ApplicationException;
import com.gpt.meetingnotes.summary.dto.AnalyzeRequest;
import com.gpt.meetingnotes.summary.dto.AnalyzeResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/mock/summary/api")
@RequiredArgsConstructor
public class TestApiMockController {
	
	private final MessageSource messageSource;
	
	@GetMapping(value = "/success", produces = "application/json; charset=UTF-8")
	public String success() {
	    return "정상 응답입니다";
	}
	
	@PostMapping(value="/analyze")
	public AnalyzeResponse procMeetingAnalyze(@Valid AnalyzeRequest analyzeRequest) {
		
		AnalyzeResponse res = new AnalyzeResponse("요약샘플");
		
		return res;
	}
	
	/**
     * 예외 테스트용 - 커스텀 예외 발생
     */
    @GetMapping(value = "/exception", produces = "application/json; charset=UTF-8")
    public String throwApplicationException() {
        throw new ApplicationException(ServiceErrorCode.SUMMARY_GENERATION_FAILED);
    }

    /**
     * 예외 테스트용 - NPE 강제 발생
     */
    @GetMapping(value = "/null", produces = "application/json; charset=UTF-8")
    public String throwNullPointer() {
        String test = null;
        return test.toString();  // NPE 발생
    }

    /**
     * 다국어 메시지 테스트
     */
    @GetMapping(value = "/i18n", produces = "application/json; charset=UTF-8")
    public String testI18n(Locale locale) {
    	log.info("locale={}", locale); 
        String message = messageSource.getMessage("response.success", null, locale);
        log.info("message={}",message);
        return message;
    }
}
