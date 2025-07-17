package com.gpt.meetingnotes.summary.controller;

import java.util.Arrays;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gpt.meetingnotes.summary.dto.AnalyzeResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController("/api/summary")
public class SummaryApiController {
	
	@PostMapping(value="/analyze")
	public ResponseEntity<AnalyzeResponse> procMeetingAnalyze() {
		log.debug("/analyze");
		
		AnalyzeResponse res = new AnalyzeResponse("2025-07-16", Arrays.asList("a","b"), "요약샘플");
		
		return ResponseEntity.ok(res);
	}
	
}
