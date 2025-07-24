package com.gpt.meetingnotes.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.gpt.meetingnotes.common.validation.JuminValidator;

@Documented
@Constraint(validatedBy ={ JuminValidator.class })
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidJumin {
	String message() default "{ValidJumin}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
