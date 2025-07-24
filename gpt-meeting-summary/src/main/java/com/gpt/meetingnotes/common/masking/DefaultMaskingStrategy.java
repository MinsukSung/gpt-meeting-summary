package com.gpt.meetingnotes.common.masking;

public class DefaultMaskingStrategy implements MaskingStrategy {
	
	private static final String MASK = "****";

    @Override
    public String mask(String input) {
        if (input == null || input.isEmpty()) {
            return MASK;
        }
        return MASK;
    }
}
