package com.gpt.meetingnotes.common.masking;

public class EmailMaskingStrategy implements MaskingStrategy {
	@Override
    public String mask(String input) {
        if (input == null || !input.contains("@")) return "****";
        String[] parts = input.split("@", 2);
        String local = parts[0];
        if (local.length() <= 2) return "**@" + parts[1];
        return local.substring(0, 2) + "****@" + parts[1];
    }
}
