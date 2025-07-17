package com.gpt.meetingnotes.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	SUCCESS("200", "response.success"),
	SERVER_ERROR("500", "error.server"),
	SERVICE_ERROR("500", "error.service"),
	
	FILE_UPLOAD_ERROR("500", "error.file.upload"),
	FILE_IO_ERROR("500", "error.file.io"),
	
	NULL_POINTER("500", "error.null"),
	
	EMPTY_FILE("400", "error.file.empty"),
	SUMMARY_FAILED("500", "error.summary.failed");
	
	private final String code;
	private final String messageKey;
	
}
