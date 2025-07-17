package com.gpt.meetingnotes.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class MessageConfig {
	
	@Bean
	public MessageSource messageSource() {
		ResourceBundleMessageSource source = new ResourceBundleMessageSource();
		source.setBasename("message/messages");
		source.setDefaultEncoding("UTF-8");
		source.setUseCodeAsDefaultMessage(true);
		return source;
	}
}
