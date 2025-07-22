package com.gpt.meetingnotes.common.enums;

import org.springframework.http.HttpStatus;

public interface ResponseCode {
	String getCode();
    String getMessageKey();

	default HttpStatus getHttpStatus() {
		return HttpStatus.OK;
	}
}
