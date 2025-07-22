package com.gpt.meetingnotes.common.enums;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SuccessCode implements ResponseCode {
	OK("S200", "common.success.ok", HttpStatus.OK),
	CREATED("S201", "common.success.created", HttpStatus.CREATED),
	ACCEPTED("S202", "common.success.accepted", HttpStatus.ACCEPTED),
	NO_CONTENT("S204", "common.success.nocontent", HttpStatus.NO_CONTENT);
	
	private final String code;
	private final String messageKey;
	private final HttpStatus httpStatus;
	
	@Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
	 
}
