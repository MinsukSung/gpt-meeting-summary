package com.gpt.meetingnotes.summary.config;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.gpt.meetingnotes") // 테스트 대상 포함되도록
public class WebMvcTestConfig {
	@Bean
	public ViewResolver viewResolver() {
	    InternalResourceViewResolver resolver = new InternalResourceViewResolver();
	    resolver.setPrefix("/WEB-INF/views/");
	    resolver.setSuffix(".jsp");
	    return resolver;
	}
	
	@Bean
	public LocaleResolver localeResolver() {
	    AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver();
	    resolver.setDefaultLocale(Locale.KOREAN);
	    return resolver;
	}
	
	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource  source = new ReloadableResourceBundleMessageSource();
		source.setBasenames("classpath:message/messages", "classpath:message/validationMessages");
		source.setDefaultEncoding("UTF-8");
	    source.setCacheSeconds(5);  //5초마다 핫리로드
	    source.setUseCodeAsDefaultMessage(true);
	    return source;
	}
	
	@Bean
    public LocalValidatorFactoryBean validator() {
        LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();
        factoryBean.setValidationMessageSource(messageSource());
        return factoryBean;
    }
	
}
