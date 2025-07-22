package com.gpt.meetingnotes.common.enums;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ServiceErrorCode implements ResponseCode {
	
	SERVICE_FAILED("S600", "error.service", HttpStatus.INTERNAL_SERVER_ERROR),
	
	FILE_UPLOAD_FAILED("S601", "error.service.fileUploadFail", HttpStatus.INTERNAL_SERVER_ERROR),
	FILE_IO_ERROR("S602", "error.service.fileIo", HttpStatus.INTERNAL_SERVER_ERROR),
    
	SUMMARY_GENERATION_FAILED("S610", "error.service.summaryFail", HttpStatus.INTERNAL_SERVER_ERROR),
	EMPTY_SUMMARY_RESULT("S611", "error.service.emptySummary", HttpStatus.BAD_REQUEST),
	
	GPT_RESPONSE_TIMEOUT("S620", "error.service.gptTimeout", HttpStatus.GATEWAY_TIMEOUT),
    
	NULL_POINTER("S630", "error.service.null", HttpStatus.INTERNAL_SERVER_ERROR);
	
	 
	private final String code;
	private final String messageKey;
	private final HttpStatus httpStatus;
}
