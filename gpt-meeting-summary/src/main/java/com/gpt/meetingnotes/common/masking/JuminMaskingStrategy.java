package com.gpt.meetingnotes.common.masking;

import com.gpt.meetingnotes.common.utils.ValidationUtils;

public class JuminMaskingStrategy implements MaskingStrategy {
	@Override
    public String mask(String input) {
		if (!ValidationUtils.isValidJumin(input)) {
	        return "****";
	    }
	    return input.replaceAll("-", "").substring(0, 6) + "-*******";
    }
}
