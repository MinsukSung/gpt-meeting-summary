package com.gpt.meetingnotes.common.masking;

public class PhoneMaskingStrategy implements MaskingStrategy {
	@Override
    public String mask(String input) {
        if (input == null || input.length() < 7) return "****";
        // 예: 010-1234-5678 -> 010-****-5678
        return input.replaceAll("(\\d{3})-(\\d{3,4})-(\\d{4})", "$1-****-$3");
    }
}
