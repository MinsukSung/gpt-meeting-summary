package com.gpt.meetingnotes.summary.mock;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gpt.meetingnotes.common.enums.ServiceErrorCode;
import com.gpt.meetingnotes.common.exception.ApplicationException;
import com.gpt.meetingnotes.summary.dto.AnalyzeRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/mock/summary")
@Controller
public class TestWebMockController {
	
	@GetMapping(value = "/")
	public String home(Locale locale, Model model) {
		log.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return "home";
	}
	
	@GetMapping(value = "/analyze")
	public String analyze(@Valid AnalyzeRequest analyzeRequest, Model model) {
		
		
		return "summary/analyze";
	}
	
	@GetMapping("/exception")
    public String triggerApplicationException() {
        throw new ApplicationException(ServiceErrorCode.SUMMARY_GENERATION_FAILED);
    }

    @GetMapping("/null")
    public String triggerNullPointer() {
        String test = null;
        return test.toString(); // NullPointerException
    }
}
