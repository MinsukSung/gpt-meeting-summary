package com.gpt.meetingnotes.summary.controller.api;

import java.util.Arrays;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gpt.meetingnotes.summary.dto.AnalyzeResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api/summary")
@RestController
public class SummaryApiController {
	
	@PostMapping(value="/analyze")
	public AnalyzeResponse procMeetingAnalyze() {
		log.debug("/analyze");
		
		AnalyzeResponse res = new AnalyzeResponse("2025-07-16", Arrays.asList("a","b"), "요약샘플");
		
		return res;
	}
	
}
