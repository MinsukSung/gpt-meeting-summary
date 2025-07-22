package com.gpt.meetingnotes.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.gpt.meetingnotes.common.enums.SuccessCode;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SuccessCodeMapping {
	SuccessCode value() default SuccessCode.OK;
}
