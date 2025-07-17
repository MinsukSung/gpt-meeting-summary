package com.gpt.meetingnotes.summary.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gpt.meetingnotes.common.enums.ErrorCode;
import com.gpt.meetingnotes.common.exception.ApplicationException;

@RequestMapping("/test")
@Controller
public class TestWebController {
	
	// 1. 커스텀 예외 발생 (ApplicationException → serviceError.jsp)
    @GetMapping("/application")
    public String throwApplicationException() {
        throw new ApplicationException(ErrorCode.EMPTY_FILE);
    }

    // 2. NullPointerException 발생 (→ serviceError.jsp 또는 500.jsp)
    @GetMapping("/null")
    public String throwNullPointerException() {
        String data = null;
        data.length();  // NPE 발생
        return "dummy";
    }

    // 3. 일반 예외 발생 (→ serviceError.jsp 또는 500.jsp)
    @GetMapping("/runtime")
    public String throwRuntimeException() {
        throw new RuntimeException("테스트용 런타임 예외");
    }
}
