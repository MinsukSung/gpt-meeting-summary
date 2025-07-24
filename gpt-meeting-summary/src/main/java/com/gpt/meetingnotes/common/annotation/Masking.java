package com.gpt.meetingnotes.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.gpt.meetingnotes.common.enums.MaskingType;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Masking {
	MaskingType type() default MaskingType.DEFAULT;
}
