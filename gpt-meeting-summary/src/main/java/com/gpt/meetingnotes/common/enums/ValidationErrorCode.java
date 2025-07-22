package com.gpt.meetingnotes.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ValidationErrorCode implements ResponseCode {
    INVALID_INPUT("V400", "error.validation.invalidInput"),
    MISSING_PARAMETER("V401", "error.validation.missingParam"),
    TYPE_MISMATCH("V402", "error.validation.typeMismatch");

    private final String code;
    private final String messageKey;
}
