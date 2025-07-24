package com.gpt.meetingnotes.config;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

@Configuration
public class MessageConfig implements WebMvcConfigurer {
	
	@Bean
	public LocaleResolver localeResolver() {
	    AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver();
	    resolver.setDefaultLocale(Locale.KOREAN);
	    return resolver;
	}
	
	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource  source = new ReloadableResourceBundleMessageSource();
		source.setBasenames("classpath:message/messages");
		source.setDefaultEncoding("UTF-8");
	    source.setCacheSeconds(5);  //5초마다 핫리로드
	    source.setUseCodeAsDefaultMessage(true);
		return source;
	}
	
	@Bean
    public LocalValidatorFactoryBean validator() {
        LocalValidatorFactoryBean validatorFactoryBean = new LocalValidatorFactoryBean();
        validatorFactoryBean.setValidationMessageSource(messageSource());
        return validatorFactoryBean;
    }

    @Override
    public Validator getValidator() {
        return validator();
    }
}
