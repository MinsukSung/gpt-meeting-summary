package com.gpt.meetingnotes.common.exception;

import com.gpt.meetingnotes.common.enums.ErrorCode;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {
	
	private final ErrorCode errorCode;
	private final String customMessage;
	
	public ApplicationException(ErrorCode errorCode) {
		super(errorCode.name());
		this.errorCode = errorCode;
		this.customMessage = null;
	}
	
	public ApplicationException(ErrorCode errorCode, String customMassage) {
		super(errorCode.name());
		this.errorCode = errorCode;
		this.customMessage = customMassage;
	}
}
