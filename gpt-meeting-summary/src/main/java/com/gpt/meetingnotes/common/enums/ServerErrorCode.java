package com.gpt.meetingnotes.common.enums;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ServerErrorCode implements ResponseCode {
	INTERNAL_ERROR("E500", "error.server.internal", HttpStatus.INTERNAL_SERVER_ERROR),
    SERVICE_UNAVAILABLE("E503", "error.server.unavailable", HttpStatus.SERVICE_UNAVAILABLE);

    private final String code;
    private final String messageKey;
    private final HttpStatus httpStatus;

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
