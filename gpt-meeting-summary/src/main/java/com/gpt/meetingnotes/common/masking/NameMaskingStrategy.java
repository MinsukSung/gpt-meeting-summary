package com.gpt.meetingnotes.common.masking;

import com.gpt.meetingnotes.common.utils.StringUtils;

public class NameMaskingStrategy implements MaskingStrategy {
	@Override
    public String mask(String input) {
		if (input == null || input.length() <= 1) return "*";

	    StringBuilder sb = new StringBuilder(input.length());
	    sb.append(input.charAt(0)); // 첫 글자 그대로 출력
	    sb.append(StringUtils.repeat('*', input.length() - 1)); // 나머지 마스킹
	    return sb.toString();
    }
}
