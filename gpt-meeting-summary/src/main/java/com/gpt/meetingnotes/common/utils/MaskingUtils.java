package com.gpt.meetingnotes.common.utils;

import java.util.EnumMap;
import java.util.Map;

import com.gpt.meetingnotes.common.enums.MaskingType;
import com.gpt.meetingnotes.common.masking.DefaultMaskingStrategy;
import com.gpt.meetingnotes.common.masking.EmailMaskingStrategy;
import com.gpt.meetingnotes.common.masking.JuminMaskingStrategy;
import com.gpt.meetingnotes.common.masking.MaskingStrategy;
import com.gpt.meetingnotes.common.masking.NameMaskingStrategy;
import com.gpt.meetingnotes.common.masking.PhoneMaskingStrategy;

public class MaskingUtils {
	private static final Map<MaskingType, MaskingStrategy> strategyMap = new EnumMap<>(MaskingType.class);

    static {
        strategyMap.put(MaskingType.DEFAULT, new DefaultMaskingStrategy());
        strategyMap.put(MaskingType.NAME, new NameMaskingStrategy());
        strategyMap.put(MaskingType.EMAIL, new EmailMaskingStrategy());
        strategyMap.put(MaskingType.PHONE, new PhoneMaskingStrategy());
        strategyMap.put(MaskingType.JUMIN, new JuminMaskingStrategy());
    }

    public static String autoMask(String original, MaskingType type) {
        if (original == null) return null;
        return strategyMap.getOrDefault(type, strategyMap.get(MaskingType.DEFAULT)).mask(original);
    }
}
