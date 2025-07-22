package com.gpt.meetingnotes.common.exception;

import com.gpt.meetingnotes.common.enums.ResponseCode;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	private final ResponseCode errorCode;
	private final String customMessage;
	
	public ApplicationException(ResponseCode errorCode) {
		super(errorCode.getCode());
		this.errorCode = errorCode;
		this.customMessage = null;
	}
	
	public ApplicationException(ResponseCode errorCode, String customMassage) {
		super(errorCode.getCode());
		this.errorCode = errorCode;
		this.customMessage = customMassage;
	}
}
