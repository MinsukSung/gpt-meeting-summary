package com.gpt.meetingnotes.summary.controller.api;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gpt.meetingnotes.summary.dto.AnalyzeRequest;
import com.gpt.meetingnotes.summary.dto.AnalyzeResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api/summary")
@RestController
public class SummaryApiController {
	
	@PostMapping(value="/analyze")
	public AnalyzeResponse procMeetingAnalyze(@Valid AnalyzeRequest analyzeRequest) {
		log.debug("/analyze");
		
		AnalyzeResponse res = new AnalyzeResponse("요약샘플");
		
		return res;
	}
	
}
