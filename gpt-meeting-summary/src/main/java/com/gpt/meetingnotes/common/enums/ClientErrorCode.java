package com.gpt.meetingnotes.common.enums;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ClientErrorCode implements ResponseCode {
	BAD_REQUEST("C400", "error.client.badRequest", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED("C401", "error.client.unauthorized", HttpStatus.UNAUTHORIZED),
    FORBIDDEN("C403", "error.client.forbidden", HttpStatus.FORBIDDEN),
    NOT_FOUND("C404", "error.client.notFound", HttpStatus.NOT_FOUND),
    METHOD_NOT_ALLOWED("405", "error.client.methodNotAllowed", HttpStatus.METHOD_NOT_ALLOWED);

    private final String code;
    private final String messageKey;
    private final HttpStatus httpStatus;

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
