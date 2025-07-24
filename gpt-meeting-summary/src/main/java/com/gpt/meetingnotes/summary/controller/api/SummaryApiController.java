package com.gpt.meetingnotes.summary.controller.api;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gpt.meetingnotes.common.annotation.SuccessCodeMapping;
import com.gpt.meetingnotes.common.enums.SuccessCode;
import com.gpt.meetingnotes.common.validation.ValidationGroups;
import com.gpt.meetingnotes.summary.dto.AnalyzeRequest;
import com.gpt.meetingnotes.summary.dto.AnalyzeResponse;
import com.gpt.meetingnotes.summary.dto.MaskingRequest;
import com.gpt.meetingnotes.summary.dto.MaskingResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api/summary")
@RestController
public class SummaryApiController {
	
	@PostMapping(value="/analyze")
	@SuccessCodeMapping(SuccessCode.OK)
	public AnalyzeResponse procMeetingAnalyze(@Validated(ValidationGroups.Analyze.class) AnalyzeRequest analyzeRequest) {
		log.debug("/analyze");
		
		AnalyzeResponse res = new AnalyzeResponse("요약샘플");
		
		return res;
	}
	
	
	@PostMapping(value="/analyze2")
	@SuccessCodeMapping(SuccessCode.OK)
	public AnalyzeResponse procMeetingAnalyze2(AnalyzeRequest analyzeRequest) {
		log.debug("/analyze2");
		
		AnalyzeResponse res = new AnalyzeResponse("요약샘플");
		
		return res;
	}
	

	@PostMapping(value="/masking")
	@SuccessCodeMapping(SuccessCode.OK)
	public MaskingResponse procMasking(@Valid @RequestBody MaskingRequest maskingRequest) {
		log.debug("/masking");
				
		MaskingResponse res = new MaskingResponse(maskingRequest.getRrn(), maskingRequest.getEmail(), maskingRequest.getPhone(), maskingRequest.getMaskingText(), maskingRequest.getText());
		
		return res;
	}
}
